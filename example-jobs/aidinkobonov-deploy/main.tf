module "helm_deploy" {
  source                 = "git::https://github.com/fuchicorp/helm-deploy.git"
  deployment_name        = "aidinkobonov-deploy"
  deployment_environment = "dev"
  deployment_endpoint    = "aidinkobonov.fuchicorp.com"
  deployment_path        = "aidinkobonov"
}