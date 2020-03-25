# module "chart" {
#   source  = "fuchicorp/chart/helm"
#   version = "0.0.2"
#   # insert the 4 required variables here
# }
module "helm_deploy" {
   source = "git::https://github.com/fuchicorp/helm-deploy.git"


  deployment_name        = "aidinkobonov-deploy"
  deployment_environment = "dev"
  deployment_endpoint    = "aidinkobonov.fuchicorp.com"
  deployment_path        = "aidinkobonov"

  template_custom_vars = {
    deployment_image = "nginx"
  }
}