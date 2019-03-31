import hudson.EnvVars

node('master'){
  stage('Check terraform') {

    try {

      env.terraform  = sh returnStdout: true, script: 'terraform --version'
      echo """

      echo Terraform already installed version ${env.terraform}

      """

    } catch(er) {
       stage('Installing Terraform') {
         sh """
         wget https://releases.hashicorp.com/terraform/0.11.11/terraform_0.11.11_linux_amd64.zip
         yum install unzip -y
         unzip terraform_0.11.11_linux_amd64.zip
         mv terraform /bin
         """

       }
    }
  }
}
