resource "kubernetes_service" "grafana-service" {
  depends_on = ["kubernetes_secret.grafana-secrets"]  
  metadata {
    name      = "grafana-service"
    namespace = "${var.namespace}"
  }

  spec {
    selector {
      app       = "grafana-deployment"
    }

    port {
      protocol    = "TCP"
      port        = 80
      target_port = 3000
    }
    
    type = "LoadBalancer"
  }
}