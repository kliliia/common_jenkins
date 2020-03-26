module "bkarakas_deploy" {

  source  = "fuchicorp/chart/helm"
  deployment_name        = "bkarakas"
  deployment_environment = "dev"
  deployment_endpoint    = "bkarakas.fuchicorp.com"
  deployment_path        = "bkarakas"
}
