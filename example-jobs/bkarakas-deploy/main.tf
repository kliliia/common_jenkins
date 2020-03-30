module "bkarakas_deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "bkarakas"
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "bkarakas"
  
}
