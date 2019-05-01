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
    stage('Checkout SCM') {
      git  'https://github.com/fuchicorp/terraform.git'
    }
    
    stage("Sending slack notification") {
      slackSend baseUrl: 'https://fuchicorp.slack.com/services/hooks/jenkins-ci/', channel: 'test-message', color: 'green', message: 'Jira job build successfull', tokenCredentialId: 'slack-token'
    }

    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/jira-deployment")
        file.write """
        namespace             =  "${namespace}"
        """
    }
    stage("Terraform init") {
      dir("${workspace}/") {
        sh "terraform init"
      }
    }
    stage("Terraform Apply/Plan"){
      if (!params.terraformDestroy) {
        if (params.terraformApply) {
          dir("${workspace}/") {
            echo "##### Terraform Applying the Changes ####"
            sh "terraform apply --auto-approve"
        }
      } else {
          dir("${WORKSPACE}/") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan"
          }
        }
      }
    }
    stage('Terraform Destroy') {
      if (!params.terraformApply) {
        if (params.terraformDestroy) {
          dir("${WORKSPACE}/") {
            echo "##### Terraform Destroying ####"
            sh "terraform destroy --auto-approve"
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

