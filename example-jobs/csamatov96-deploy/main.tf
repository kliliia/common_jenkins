module "helm_deploy" {
  source                 = "fuchicorp/chart/helm"
  deployment_name        = "csamatov96-deploy" #chart name 
  deployment_environment = "dev"
  deployment_endpoint    = "${var.deployment_endpoint}"
  deployment_path        = "csamatov96-deploy"
}
