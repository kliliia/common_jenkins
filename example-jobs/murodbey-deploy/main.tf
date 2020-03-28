module "murodbey_deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "murodbey"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "murodbey"
}