
/*
* Script to randomly select username for FuchiCorp Projects
* Author Farkhod Sadykov
* email: sadykovfarkhod@gmail.com
*/

// ["apenjiyev", "beamsoul", "chaglare", "daudmu21", "fahriddin23", "jipara", "jsartbaeva90", "Khuslentuguldur", "LeilaDev", "leventelibal", "Madina89", "mcalik77", "NadiraSaip", "Nurjan87", "rootvovak"]
// ["Abdul", "Nurjamal", "Nodira", "Florin", "Murodbek", "Alibek", "Aidin", "Akmal", "AndrewZ", "AndrewK", "Sonya"]

def notifySuccessful() {
    def random = new Random();
    def fuchiCorpUsers = ["apenjiyev", "beamsoul", "chaglare", "daudmu21", "fahriddin23", "jipara", "jsartbaeva90", "Khuslentuguldur", "LeilaDev", "leventelibal", "Madina89", "mcalik77", "NadiraSaip", "Nurjan87", "rootvovak"]

    def randomKey = random.nextInt(fuchiCorpUsers.size())
    println("${fuchiCorpUsers[randomKey]} you are sellected.")
    slackTokenId = 'slack-token'
    slacklink    = 'https://fuchicorp.slack.com/services/hooks/jenkins-ci/'
    def ticketNumber = "${params.issueUrl}".replace('/', ' ').split(' ')[-1]
    def ticketLink   = "${params.issueUrl}"

    node {
      properties([
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
