def SELECTED_BRANCH
def DEPLOYMENT
pipeline {
    agent any
    stages{
        stage("Cleanup") {
            steps {
                script {
                    println("${bot_name}")

                }
            }
        }
        stage ("Select branch") {
            steps {
                script {
                    DEPLOYMENT = getApplicationDeployments(bot_name)
                    def branchesAsString = ""
                    propertyList = []
                    branches = [];
                    withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'token')]) {
                        def githubRestUrl = getGithubRestURL(DEPLOYMENT.githubUrl, token)
                        println(githubRestUrl)
                        def output = sh(script: "git ls-remote --heads ${githubRestUrl}", returnStdout: true)
                        branches = getAllGitBranches(output)
                    }
                    propertyList.add(getChoiceParameterDefinition('branch',branches))
                    timeout(time: 2, unit : 'MINUTES') {
                        SELECTED_BRANCH = input(id: 'baseChoices', message: 'Set params', parameters: propertyList)
                    }

                    println("Branch ${SELECTED_BRANCH}")
                }
            }
        }
    }
}

def getAllGitBranches(scriptOutput) {
    def branches = ['main']
    def anotherBranches = scriptOutput.readLines().collect{it.split()[1].replaceAll("refs/heads/", "")}.sort().reverse()
    for (String element in anotherBranches) {
        if ("main" != element) {
            branches.add(element)
        }
    }
    return branches
}

def getChoiceParameterDefinition(name, values) {
    return [$class: 'ChoiceParameterDefinition', choices: values, description: 'Choose an Operation', name: name]
}

def getApplicationDeployments(applicationName) {
    switch (applicationName) {
        case "autoban_telegram_bot" :
            return new Deployment(applicationName, "https://github.com/sizov2405/autoban_telegram_bot.git")
        case "autocomment_telegram_bot":
            return new Deployment(applicationName, "https://github.com/sizov2405/autocomment_telegram_bot.git")
        case "add_text_telegram_bot" :
            return  new Deployment(applicationName, "https://github.com/sizov2405/add_bot_text_bot.git")
        default :
            return null
    }
}

def getGithubRestURL(githubUrl, token) {
    def trimmedUrl = githubUrl.replaceFirst("https://", "")
    return "https://${token}@${trimmedUrl}"
}

class Deployment {
    String applicationName
    String githubUrl

    Deployment(applicationName, githubUrl) {
        this.applicationName = applicationName
        this.githubUrl = githubUrl
    }
}