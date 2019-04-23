resource "kubernetes_persistent_volume_claim" "grafana-pvc" {
  depends_on = ["kubernetes_secret.grafana-secrets"]

  metadata {
    name      = "grafana-pvc"
    namespace = "${var.namespace}"

    labels {
      app = "grafana-deployment"
    }
  }

  spec {
    access_modes = ["ReadWriteOnce"]

    resources {
      requests {
        storage = "5Gi"
      }
    }
  }
}