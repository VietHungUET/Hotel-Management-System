output "artifact_registry_repository" {
  value = google_artifact_registry_repository.backend.name
}

output "cloud_sql_instance_connection_name" {
  value = google_sql_database_instance.postgres.connection_name
}

output "cloud_run_runtime_service_account" {
  value = google_service_account.cloud_run_runtime.email
}

output "backend_url" {
  value = google_cloud_run_v2_service.backend.uri
}