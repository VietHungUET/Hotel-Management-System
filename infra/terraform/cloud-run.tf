resource "google_cloud_run_v2_service" "backend" {
  project  = var.project_id
  name     = "hms-backend"
  location = var.region

  ingress             = "INGRESS_TRAFFIC_ALL"
  deletion_protection = false

  template {
    service_account                  = google_service_account.cloud_run_runtime.email
    max_instance_request_concurrency = 20
    timeout                          = "300s"

    scaling {
      min_instance_count = 0
      max_instance_count = 2
    }

    containers {
      image = var.backend_image

      ports {
        container_port = 8080
      }

      resources {
        limits = {
          cpu    = "1"
          memory = "1Gi"
        }

        cpu_idle          = true
        startup_cpu_boost = true
      }

      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "cloud"
      }

      env {
        name  = "DB_URL"
        value = "jdbc:postgresql:///${var.db_name}?cloudSqlInstance=${google_sql_database_instance.postgres.connection_name}&socketFactory=com.google.cloud.sql.postgres.SocketFactory&cloudSqlRefreshStrategy=lazy"
      }

      env {
        name  = "DB_USERNAME"
        value = var.db_user
      }

      env {
        name  = "CORS_ALLOWED_ORIGINS"
        value = var.cors_allowed_origins
      }

      env {
        name  = "GCP_PROJECT_ID"
        value = var.project_id
      }

      env {
        name  = "DB_MAX_POOL_SIZE"
        value = "5"
      }

      env {
        name  = "DB_MIN_IDLE"
        value = "1"
      }

      dynamic "env" {
        for_each = {
          DB_PASSWORD    = "hms-db-password"
          JWT_SECRET     = "hms-jwt-secret"
          EMAIL_USERNAME = "hms-email-username"
          EMAIL_PASSWORD = "hms-email-password"
          GEMINI_API_KEY = "hms-gemini-api-key"
        }

        content {
          name = env.key

          value_source {
            secret_key_ref {
              secret  = env.value
              version = "latest"
            }
          }
        }
      }

      startup_probe {
        initial_delay_seconds = 10
        timeout_seconds       = 5
        period_seconds        = 5
        failure_threshold     = 30

        http_get {
          path = "/actuator/health/liveness"
          port = 8080
        }
      }

      liveness_probe {
        initial_delay_seconds = 30
        timeout_seconds       = 5
        period_seconds        = 30
        failure_threshold     = 3

        http_get {
          path = "/actuator/health/liveness"
          port = 8080
        }
      }
    }
  }

  depends_on = [
    google_project_service.required,
    google_project_iam_member.cloud_sql_client,
    google_project_iam_member.firestore_user,
    google_secret_manager_secret_iam_member.runtime_access,
    google_firestore_database.default,
    google_sql_database.hms
  ]
}

resource "google_cloud_run_v2_service_iam_member" "backend_public" {
  project  = var.project_id
  location = google_cloud_run_v2_service.backend.location
  name     = google_cloud_run_v2_service.backend.name

  role   = "roles/run.invoker"
  member = "allUsers"
}
