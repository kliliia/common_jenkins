import hudson.EnvVars

pipeline {
    agent  any
    stages {

        try {

          stage('Check for terraform') {
            env.terraform  = sh returnStdout: true, script: 'terraform --version'
            if (env.terraform) { sh "echo Terraform already installed version ${env.terraform}" }
          }

        } catch(er) {

        stage ('Install the terraform') {
            steps {

                sh """
                wget https://releases.hashicorp.com/terraform/0.11.11/terraform_0.11.11_linux_amd64.zip
                unzip terraform_0.11.11_linux_amd64.zip
                mv terraform /bin
                """
            }
        }
      }
    }
}
