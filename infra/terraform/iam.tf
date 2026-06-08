resource "google_service_account" "cloud_run_runtime" {
  project      = var.project_id
  account_id   = "hms-cloud-run-runtime"
  display_name = "HMS Cloud Run runtime"
}

resource "google_project_iam_member" "cloud_sql_client" {
  project = var.project_id
  role    = "roles/cloudsql.client"
  member  = "serviceAccount:${google_service_account.cloud_run_runtime.email}"
}

resource "google_project_iam_member" "firestore_user" {
  project = var.project_id
  role    = "roles/datastore.user"
  member  = "serviceAccount:${google_service_account.cloud_run_runtime.email}"
}
