module "tunji57_deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "tunji57"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "tunji57"
}