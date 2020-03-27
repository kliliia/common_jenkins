module "mkarimi_deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "mkarimi"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "deploy"
}