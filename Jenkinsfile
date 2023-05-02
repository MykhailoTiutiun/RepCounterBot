pipeline {
    agent any

    stages {
        stage('Update From GiHub'){
            steps{
               git 'https://github.com/MykhailoTiutiun/RepCounterBot'
            }
        }
        stage('Build') {
            steps {
                // Run Maven on a Unix agent.
                sh "mvn clean package"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
        stage('Deploy') {
            steps {
                sh "docker-compose -f docker-compose.yml up -d"
                sh "docker restart tomcat"
            }
        }
    }
}
