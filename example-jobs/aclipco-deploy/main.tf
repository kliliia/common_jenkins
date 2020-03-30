module "aclipco_deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "aclipco"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "aclipco"

}