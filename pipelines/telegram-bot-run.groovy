pipeline {
    agent any
    stages{
        stage('Cleanup') {
            steps {
                script {
                    println(${bot_name})
                }
            }
        }
    }
}