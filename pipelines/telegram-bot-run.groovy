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
                    def branchesAsString = ""
                    withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'token')]) {
                        def output = sh(script: "git ls-remote --heads https://${token}@github.com/sizov2405/autoban_telegram_bot.git", returnStdout: true)
                        println(output)
                    }
                }
            }
        }
    }
}

def getAllGitBranches(scriptOutput) {
    def branches = ['main']
    def anotherBranches = scriptOutput.readLines().collect{it.cplit()[1].replaceAll("refs/heads/", "")}.sort().reverse()
    for (String element in anotherBranches) {
        if ("main" != element) {
            branches.add(element)
        }
    }
    return branches.join("\n")
}

def getChoiceParameterDefinition() {
    return [$class: 'ChoiceParameterDefinition', choices: ['ADD','SHOW','DELETE','UPDATE','RESET'], description: 'Choose an Operation', name: '']
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

class Deployment {
    String applicationName
    String githubUrl

    Deployment(applicationName, githubUrl) {
        this.applicationName = applicationName
        this.githubUrl = githubUrl
    }
}