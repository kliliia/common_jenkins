module "hakten_deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "hakten"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "hakten"
}