#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([
    booleanParam(defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'),
    booleanParam(defaultValue: false, description: 'Destroy All', name: 'terraformDestroy'),  
    string(defaultValue: 'default_token', description: 'Please provide a token for vault', name: 'secret', trim: true),
    string(defaultValue: 'test', description: 'Please provide namespace for vault-deployment', name: 'namespace', trim: true)
    ]
    )])
    stage('Checkout SCM') {
      git branch: 'murodbey', url: 'https://github.com/fuchicorp/terraform.git'
    } 
    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/google_vault/vault.tfvars")
        file.write """
        secret                   =  "${secret}"
        namespace                =  "${namespace}"

        """
      }
    stage("Sending slack notification") {
      slackSend baseUrl: 'https://fuchicorp.slack.com/services/hooks/jenkins-ci/', 
      channel: 'test-message', 
      color: '"#00FF00"', 
      message: 'The vault deployment job is build successful', 
      tokenCredentialId: 'slack-token' 
    }
    stage("Terraform init") {
      dir("${workspace}/google_vault/") {
        sh "terraform init"
      }
    }
        stage("Terraform Apply/Plan"){
      if (!params.terraformDestroy) {
        if (params.terraformApply) {
          dir("${workspace}/google_vault/") {
            echo "##### Terraform Applying the Changes ####"
            sh "terraform apply --auto-approve -var-file=vault.tfvars"
        }
      } else {
          dir("${WORKSPACE}/google_vault") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=vault.tfvars"
          }
        }
      } 
    }
    stage('Terraform Destroy') {
      if (!params.terraformApply) {
        if (params.terraformDestroy) {
          dir("${WORKSPACE}/google_vault") {
            echo "##### Terraform Destroying ####"
            sh "terraform destroy --auto-approve -var-file=vault.tfvars"
          }
        } 
      }
    }
       if (params.terraformDestroy) {
         if (params.terraformApply) {
           println("""
           Sorry you can not destroy and apply at the same time
           """)
        }
    }
}
