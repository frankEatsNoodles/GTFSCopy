pipeline {
    agent any

    tools {
        jdk 'jdk21'
        maven 'maven3'
    }

    environment {
        REGISTRY = 'host.docker.internal:8082'
        IMAGE_NAME = 'transitdata'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests -Dspring.profiles.active=deploy'
            }
        }
    }

    post {
        success {
            emailext(
                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """Good news. Your build was successful.
                \nJob: ${env.JOB_NAME}
                \nDev URL: ${env.BUILD_URL}""",
                to: "56frankwu@gmail.com"
            )
        }

        failure {
            emailext(
                subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """Your build failed.
                \nCheck details here: ${env.BUILD_URL}""",
                to: "56frankwu@gmail.com"
            )
        }
    }
}