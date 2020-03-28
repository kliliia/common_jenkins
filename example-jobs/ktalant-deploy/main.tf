module "talant_app_module" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "talant"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "talant-deploy"
}
