module "talant_module" {

  source                 = "fuchicorp/chart/helm"
  deployment_name        = "alisait"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "alisait"
}
