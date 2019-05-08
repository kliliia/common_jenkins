
/*
* Script to randomly select username for FuchiCorp Projects
* Author Farkhod Sadykov
* email: sadykovfarkhod@gmail.com
*/

def notifySuccessful() {
    def random = new Random();
    def fuchiCorpUsers = ["Abdul", "Nurjamal", "Nodira", "Florin", "Murodbek", "Alibek", "Aidin", "Akmal", "AndrewZ", "AndrewK", "Sonya"]

    def randomKey = random.nextInt(fuchiCorpUsers.size())
    println("${fuchiCorpUsers[randomKey]} you are sellected.")
    slackTokenId = 'slack-token'
    slacklink    = 'https://fuchicorp.slack.com/services/hooks/jenkins-ci/'
    def ticketNumber = "${params.issueUrl}".replace('/', ' ').split(' ')[-1]
    def ticketLink   = "${params.issueUrl}"

    node {
      properties([[
        $class: 'JiraProjectProperty'],
        parameters([string(defaultValue: 'https://github.com/fuchicorp/main-fuchicorp/issues/20',
        description: 'Please provide which ticker you want to assign.',
        name: 'issueUrl', trim: true)])])

      stage('Send Slack') {
        slackSend (color: '#00FF00', baseUrl : "${slacklink}".toString(), channel: 'devops', tokenCredentialId: "${slackTokenId}".toString(),
        message: """ Jenkins Random User Selection.
        *${fuchiCorpUsers[randomKey]}* you were sellected,
        Please let the team know if can not work on this ticket. thank you !!!
        Ticket Number: ${ticketNumber}
        Ticket URL ${ticketLink}
        USERSELECTED *${fuchiCorpUsers[randomKey]}*
        email: fuchicorpsolution@gmail.com""".replace('        ', ''))
      }
    }
}

notifySuccessful()
