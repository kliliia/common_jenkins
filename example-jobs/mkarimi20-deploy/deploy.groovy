def k8slabel = "jenkins-pipeline-${UUID.randomUUID().toString()}"
def slavePodTemplate = """
      metadata:
        labels:
          k8s-label: ${k8slabel}
        annotations:
          jenkinsjoblabel: ${env.JOB_NAME}-${env.BUILD_NUMBER}
      spec:
        affinity:
          podAntiAffinity:
            requiredDuringSchedulingIgnoredDuringExecution:
            - labelSelector:
                matchExpressions:
                - key: component
                  operator: In
                  values:
                  - jenkins-jenkins-master
              topologyKey: "kubernetes.io/hostname"
        containers:
        - name: docker
          image: docker:latest
          imagePullPolicy: Always
          command:
          - cat
          tty: true
          volumeMounts:
            - mountPath: /var/run/docker.sock
              name: docker-sock
            - mountPath: /etc/secrets/service-account/
              name: google-service-account
        - name: fuchicorptools
          image: fuchicorp/buildtools
          imagePullPolicy: Always
          command:
          - cat
          tty: true
          volumeMounts:
            - mountPath: /var/run/docker.sock
              name: docker-sock
            - mountPath: /etc/secrets/service-account/
              name: google-service-account
        serviceAccountName: common-service-account
        securityContext:
          runAsUser: 0
          fsGroup: 0
        volumes:
          - name: google-service-account
            secret:
              secretName: google-service-account
          - name: docker-sock
            hostPath:
              path: /var/run/docker.sock
    """
    
    podTemplate(name: k8slabel, label: k8slabel, yaml: slavePodTemplate) {
      node(k8slabel) {
        properties([
            parameters([
                booleanParam(defaultValue: false, description: 'Do you want to apply changes?', name: 'TERRAFORM_APPLY')
                ])
            ])
 
        stage("Pull SCM") {
          git branch: 'deploy-mkarimi', url: 'https://github.com/fuchicorp/common_jenkins.git'
        }

        stage("Apply/Plan")  {
            container("fuchicorptools") {
                dir("${WORKSPACE}/example-jobs/mkarimi20-deploy") {
                    sh 'terraform init'
                    if("${TERRAFORM_APPLY}" == "true") {
                        println("I will apply changes")
                        sh "terraform apply -auto-approve"
                    } else {
                        println("I will plan the changes")
                        sh "terraform plan"
                    }
                }
               
            }
        }
      }
    }