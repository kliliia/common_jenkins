#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([
    booleanParam(defaultValue: false, description: 'Plan before apply', name: 'terraformPlan'), 
    booleanParam(defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'), 
    booleanParam(defaultValue: false, description: 'Destroy All', name: 'terraformDestroy'), 
    string(defaultValue: 'default_token', description: 'Please provide a token for vault', name: 'vault_token', trim: true)
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
      if (params.terraformPlan) {
        dir("${workspace}/vaultDeployment/") {
          sh "terraform plan -var-file=vault.tfvars"
        }
      } else if (!params.terraformDestroy) {
      if (params.terraformApply) {
          dir("${workspace}/vaultDeployment/") {
            sh "terraform apply --auto-approve -var-file=vault.tfvars"
            }
          }
        } else if (!params.terraformApply) {
      if (params.terraformDestroy) {
         dir("${workspace}/vaultDeployment/") {
            sh "terraform destroy --auto-approve -var-file=vault.tfvars"
          }
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
