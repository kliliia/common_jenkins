module "seedoffd_deploy" {
  source  = "fuchicorp/chart/helm"
  deployment_name        = "seedoffd"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "seedoffd"
}