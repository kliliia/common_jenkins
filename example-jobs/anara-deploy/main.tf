module "anara-deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "anara"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "anara2303"

}