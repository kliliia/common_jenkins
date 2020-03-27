module "bkarakas_deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "bkarakas-deploy"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "bkarakas"
  
}
