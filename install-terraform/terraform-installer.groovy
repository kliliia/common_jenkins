pipeline {
    agent  any
    stages {
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
