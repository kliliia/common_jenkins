#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([ 
    booleanParam(defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'), 
    booleanParam(defaultValue: false, description: 'Destroy All', name: 'terraformDestroy'), 
    string(defaultValue: 'test', description: 'Please provide namespace for jira-deployment', name: 'namespace', trim: true)
    ]
    )])
    checkout scm
    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/jiraDeployment/jira.tfvars")
        file.write """
        namespace             =  "${namespace}"
        """
      }
    stage("Terraform init") {
      dir("${workspace}/jiraDeployment/") {
        sh "terraform init"
      }
    }
    stage("Terraform Apply/Plan"){
      if (!params.terraformDestroy) {
        if (params.terraformApply) {
          dir("${workspace}/jiraDeployment/") {
            echo "##### Terraform Applying the Changes ####"
            sh "terraform apply --auto-approve -var-file=jira.tfvars"
        }
      } else {
          dir("${WORKSPACE}/jiraDeployment") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=jira.tfvars"
          }
        }
      } 
    }
    stage('Terraform Destroy') {
      if (!params.terraformApply) {
        if (params.terraformDestroy) {
          dir("${WORKSPACE}/jiraDeployment") {
            echo "##### Terraform Destroying ####"
            sh "terraform destroy --auto-approve -var-file=jira.tfvars"
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