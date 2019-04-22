#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([ 
    booleanParam(defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'), 
    booleanParam(defaultValue: false, description: 'Destroy All', name: 'terraformDestroy'), 
    string(defaultValue: 'test', description: 'Please provide namespace for nexus-deployment', name: 'namespace', trim: true)
    ]
    )])
    checkout scm
    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/nexusDeployment/nexus.tfvars")
        file.write """
        namespace             =  "${namespace}"
        """
      }
    stage("Terraform init") {
      dir("${workspace}/nexusDeployment/") {
        sh "terraform init"
      }
    }
    stage("Terraform Apply/Plan"){
      if (!params.terraformDestroy) {
        if (params.terraformApply) {
          dir("${workspace}/nexusDeployment/") {
            echo "##### Terraform Applying the Changes ####"
            sh "terraform apply --auto-approve -var-file=nexus.tfvars"
        }
      } else {
          dir("${WORKSPACE}/nexusDeployment") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=nexus.tfvars"
          }
        }
      } 
    }
    stage('Terraform Destroy') {
      if (!params.terraformApply) {
        if (params.terraformDestroy) {
          dir("${WORKSPACE}/nexusDeployment") {
            echo "##### Terraform Destroying ####"
            sh "terraform destroy --auto-approve -var-file=nexus.tfvars"
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