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

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build \
                        -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Push to Nexus') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-docker',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASSWORD'
                    )
                ]) {
                    sh '''
                        set +x

                        echo "$NEXUS_PASSWORD" | docker login "$REGISTRY" \
                            -u "$NEXUS_USER" --password-stdin

                        docker tag ${IMAGE_NAME}:${BUILD_NUMBER} \
                            ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}

                        docker push ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}

                        docker logout "$REGISTRY"

                        docker rmi \
                            ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} \
                            ${IMAGE_NAME}:${BUILD_NUMBER}
                    '''
                }
            }
        }
    }

    post {
        success {
            emailext(
                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Good news. Your build succeeded.\\n\\nJob: ${env.JOB_NAME}\\nBuild: ${env.BUILD_NUMBER}\\nImage: ${env.REGISTRY}/${env.IMAGE_NAME}:${env.BUILD_NUMBER}\\nURL: ${env.BUILD_URL}",
                to: "56frankwu@gmail.com"
            )
        }

        failure {
            emailext(
                subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Your build failed.\\n\\nCheck details here: ${env.BUILD_URL}",
                to: "56frankwu@gmail.com"
            )
        }
    }
}