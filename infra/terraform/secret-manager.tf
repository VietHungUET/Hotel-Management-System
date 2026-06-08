locals {
  secret_names = toset([
    "hms-db-password",
    "hms-jwt-secret",
    "hms-email-username",
    "hms-email-password",
    "hms-gemini-api-key"
  ])
}

resource "google_secret_manager_secret" "app" {
  for_each = local.secret_names

  project   = var.project_id
  secret_id = each.value

  replication {
    auto {}
  }

  depends_on = [
    google_project_service.required
  ]
}

resource "google_secret_manager_secret_iam_member" "runtime_access" {
  for_each = google_secret_manager_secret.app

  project   = var.project_id
  secret_id = each.value.id
  role      = "roles/secretmanager.secretAccessor"
  member    = "serviceAccount:${google_service_account.cloud_run_runtime.email}"
}