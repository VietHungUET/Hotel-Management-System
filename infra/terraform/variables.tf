variable "project_id" {
  description = "Google Cloud project ID"
  type        = string
}

variable "region" {
  description = "Primary Google Cloud region"
  type        = string
  default     = "asia-southeast1"
}

variable "db_instance_name" {
  type    = string
  default = "hms-postgres"
}

variable "db_name" {
  type    = string
  default = "hms"
}

variable "db_user" {
  type    = string
  default = "hms_app"
}

variable "backend_image" {
  description = "Backend container image to deploy to Cloud Run"
  type        = string
}

variable "cors_allowed_origins" {
  description = "Comma-separated frontend origins allowed by backend CORS"
  type        = string
  default     = "http://localhost:5173"
}