
@Library('CommonLib@master') _

def buildChecker         = [:]
def common               = new com.mcd.CommonEmail()
def applyCleaning        = params['ApplyCleaning']
def defaultDayToDelete   = 10

// run on master node
node('master') {

  checkout scm
  buildChecker['checkForPercent'] = 70



  // check local disk usage
  buildChecker['size'] = sh(returnStdout: true, script: '''df -h | grep jenkins_home |awk '{print $5 + ""}' ''' )
  if (!applyCleaning) {
    if (buildChecker['size'].toInteger() > buildChecker['checkForPercent']) {
        withCredentials([string(credentialsId: 'vet-alert-webex-teams-token', variable: 'TOKEN')]) {

          // Send alert to webex teams room < VET Alerts >
          sh  '''curl -X POST 'https://api.ciscospark.com/v1/messages' \
            -H "Authorization: Bearer ${TOKEN}"\
            -H 'Content-Type: application/json' \
            -d '{ "roomId": "Y2lzY29zcGFyazovL3VzL1JPT00vNDU3ZTViNDAtNDk5ZS0xMWU5LWIyNzMtZWI5Y2RlMGVlMTBh", \
            "markdown": "# The Jenkins disk usage is over %70 percent \\nThis job will delete  `workspace`  automatically. Please follow the\\nsteps to clean up the Jenkins's disk usage manually.  \\n1.  Click on the [link](https://jenkins.sharedtools.vet-tools.digitalecp.mcd.com/job/jenkins-cleaner/) \\n2.  Click Build with Parameters \\n3.  Before you build the job make sure you click on the `ApplyCleaning` and select `daysSelection`"}'
            '''
        }


        // if cleaning does not applied and disk usage more than expected jenkins will send email
        currentBuild.result = 'FAILURE'
        currentBuild.description = """Jenkins disk usage is increased to ${buildChecker['size']}."""
        common.sendEmailToUsVetTeam()
        applyCleaning = true

    } else {
      println("""The disk usage is below the ${buildChecker['checkForPercent']}% threshold. Cleaning procedures skipped.""")
    }
  }

  // if applied cleaning
  if (applyCleaning) {

    stage('Clean local dir') {

      // find and delete all jobs older than given days from params
      sh "find $JENKINS_HOME/workspace -maxdepth 1 -type d  -mtime +${daysSelection} -exec rm -rv {} +"
    }
  }
}
