resource "google_artifact_registry_repository" "backend" {
  project       = var.project_id
  location      = var.region
  repository_id = "hms-containers"
  description   = "Hotel Management System container images"
  format        = "DOCKER"

  depends_on = [
    google_project_service.required
  ]
}