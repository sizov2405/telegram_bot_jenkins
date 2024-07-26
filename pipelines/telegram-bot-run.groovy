pipeline {
    environment{

    }
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