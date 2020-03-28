module "helm_deploy" {
  source                 = "fuchicorp/chart/helm"
  deployment_name        = "aidinkobonov-deploy"
  deployment_environment = "dev"
  deployment_endpoint    = "aidinkobonov-deploy.fuchicorp.com"
  deployment_path        = "aidinkobonov-deploy"
}