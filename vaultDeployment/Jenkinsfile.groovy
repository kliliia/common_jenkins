#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([
    booleanParam(defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'),  
    string(defaultValue: 'default_token', description: 'Please provide a token for vault', name: 'vault_token', trim: true),
    string(defaultValue: 'test', description: 'Please provide namespace for vault-deployment', name: 'namespace', trim: true)
    ]
    )])
    checkout scm
    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/vaultDeployment/vault.tfvars")
        file.write """
        vault_token              =  "${vault_token}"
        namespace                =  "${namespace}"

        """
      }
    stage("Terraform init") {
      dir("${workspace}/vaultDeployment/") {
        sh "terraform init"
      }
    }
    stage('Terraform Apply/Plan') {
      if (params.terraformApply) {
        dir("${WORKSPACE}/vaultDeployment/") {
          echo "##### Terraform Applying the Changes ####"
          sh "terraform apply  --auto-approve  -var-file=vault.tfvars"
        }
    } else {
        dir("${WORKSPACE}/vaultDeployment/") {
          echo "##### Terraform Plan (Check) the Changes ####"
          sh "terraform plan -var-file=vault.tfvars"
        }
      } 
    }
    stage('Terraform Destroy') {
      if (!params.terraformApply) {
        if (params.terraformDestroy) {
          dir("${WORKSPACE}/vaultDeployment") {
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
