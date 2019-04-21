#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([
    string(defaultValue: 'plan', description: 'Please provide what action you want? (plan,apply,destroy)', name: 'terraformPlan', trim: true),
    string(defaultValue: 'default_token_add_here', description: 'Please provide a token for vault', name: 'vault_token', trim: true)
    ]
    )])
    checkout scm
    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/vaultDeployment/vault.tfvars")
        file.write """
        vault_token              =  "${vault_token}"

        """
      }
    stage("Terraform init") {
      dir("${workspace}/vaultDeployment/") {
        sh "terraform init"
      }
    stage("Terraform Plan/Apply/Destroy"){
      if (params.terraformPlan.toLowerCase() == 'plan') {
        dir("${workspace}/vaultDeployment/") {
          sh "terraform plan -var-file=vault.tfvars"
        }
      }
      if (params.terraformPlan.toLowerCase() == 'apply') {
          dir("${workspace}/vaultDeployment/") {
            sh "terraform apply --auto-approve -var-file=vault.tfvars"
          }
        }

      if (params.terraformPlan.toLowerCase() == 'destroy') {
         dir("${workspace}/vaultDeployment/") {
            sh "terraform destroy --auto-approve -var-file=vault.tfvars"
          }
      } else {
        println("""
              Sorry I don`t understand ${params.terraformPlan}!!!
              Please provide correct option (plan/apply/destroy)
              """)
      }
    }
  }
}
