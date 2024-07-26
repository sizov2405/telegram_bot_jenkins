pipeline {
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