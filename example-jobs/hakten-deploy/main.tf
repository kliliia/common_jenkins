module "grafana_deploy" {
  source  = "fuchicorp/chart/helm"
  deployment_name        = "hakten"
  deployment_environment = "dev"
  deployment_endpoint    = "hakten.fuchicorp.com"
  deployment_path        = "hakten"



}