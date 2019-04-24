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
    stage('Checkout SCM') {
      git branch: 'andrey', url: 'https://github.com/fuchicorp/terraform.git'
    }  
    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/nexus/nexus.tfvars")
        file.write """
        namespace             =  "${namespace}"
        """
      }
    stage("Terraform init") {
      dir("${workspace}/nexus/") {
        sh "terraform init"
      }
    }
    stage("Terraform Apply/Plan"){
      if (!params.terraformDestroy) {
        if (params.terraformApply) {
          dir("${workspace}/nexus/") {
            echo "##### Terraform Applying the Changes ####"
            sh "terraform apply --auto-approve -var-file=nexus.tfvars"
        }
      } else {
          dir("${WORKSPACE}/nexus") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=nexus.tfvars"
          }
        }
      } 
    }
    stage('Terraform Destroy') {
      if (!params.terraformApply) {
        if (params.terraformDestroy) {
          dir("${WORKSPACE}/nexus") {
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
