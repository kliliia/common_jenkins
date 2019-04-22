resource "kubernetes_deployment" "grafana-deployment" {
  depends_on = ["kubernetes_secret.grafana-secrets"]

  metadata {
    name      = "grafana-deployment"
    namespace = "${var.namespace}"

    labels {
      app       = "grafana-deployment"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels {
        app = "grafana-deployment"
      }
    }

    template {
      metadata {
        labels {
          app = "grafana-deployment"
        }
      }

      spec {
        volume {
          name = "grafana-pvc"

          persistent_volume_claim {
            claim_name = "grafana-pvc"
          }
        }

        container {
          name  = "grafana-deployment"
          image = "grafana/grafana:4.2.0"

          port {
            container_port = 3000
            protocol       = "TCP"
          }

          env {
            name  = "GF_AUTH_BASIC_ENABLED"
            value = "true"
          }
          env {
            name = "GF_SECURITY_ADMIN_USER"
            value_from {
              secret_key_ref {
                name = "grafana-secrets"
                key  = "username"
              }
            }
          }
          env {
            name = "GF_SECURITY_ADMIN_PASSWORD"
            value_from {
              secret_key_ref {
                name = "grafana-secrets"
                key  = "password"
              }
            }
          }
          env {
            name  = "GF_AUTH_ANONYMOUS_ENABLED"
            value = "false"
          }
          resources {
            limits {
              cpu    = "100m"
              memory = "100Mi"
            }
            requests {
              cpu    = "100m"
              memory = "100Mi"
            }
          }
          volume_mount {
            name       = "grafana-pvc"
            mount_path = "/var/lib/grafana"
          }
        #   readiness_probe {
        #     http_get {
        #       path = "/login"
        #       port = "3000"
        #     }
        #   }
          image_pull_policy = "IfNotPresent"
        }
      }
    }
  }
}
