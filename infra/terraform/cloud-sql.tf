resource "google_sql_database_instance" "postgres" {
  project          = var.project_id
  name             = var.db_instance_name
  region           = var.region
  database_version = "POSTGRES_16"

  deletion_protection = false

  settings {
    tier              = "db-f1-micro"
    edition           = "ENTERPRISE"
    availability_type = "ZONAL"

    disk_type       = "PD_SSD"
    disk_size       = 10
    disk_autoresize = true

    connector_enforcement = "REQUIRED"

    backup_configuration {
      enabled                        = true
      point_in_time_recovery_enabled = true
      start_time                     = "20:00"
    }

    ip_configuration {
      ipv4_enabled = true
    }
  }

  depends_on = [
    google_project_service.required
  ]
}

resource "google_sql_database" "hms" {
  project  = var.project_id
  name     = var.db_name
  instance = google_sql_database_instance.postgres.name
}