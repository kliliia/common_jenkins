module "helm_deploy" {
  source                 = "git::https://github.com/fuchicorp/helm-deploy.git"
  deployment_name        = "example-deployment"
  deployment_environment = "dev"
  deployment_endpoint    = "csamatov96.fuchicorp.com"
  deployment_path        = "example"

  template_custom_vars = {
    //deployment_image = "nginx"
  }
}