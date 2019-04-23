resource "kubernetes_secret" "grafana-secrets" {
  metadata {
    name      = "grafana-secrets"
    namespace = "${var.namespace}"
  }
  data {
    username = "admin"
    password = "${var.password}"
  }
  type = "Opaque"
}

