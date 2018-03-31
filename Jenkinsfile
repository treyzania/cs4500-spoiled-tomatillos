pipeline {
	agent {
		docker {
			image 'maven:3-alpine'
			args '-v /root/.m2:/root/.m2'
		}
	}
	stages {
		stage('Build') {
			steps {
				echo "Building"
				sh 'mvn compile'
				sh 'mvn package'
			}
		}
		stage('Test') {
			steps {
				echo "Testing"
				sh 'mvn test'
			}
		}
		stage('SonarQube') {
            steps {
                sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true'
                sh 'mvn sonar:sonar -Dsonar.host.url=http://ec2-18-220-143-170.us-east-2.compute.amazonaws.com:9000/'
            }
        }
        stage('Quality') {
            steps {
                sh 'sleep 30'
                timeout(time: 10, unit: 'SECONDS') {
                    retry(5) {
                        script {
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                error "Pipeline aborted due to quality gate failure: ${qg.status}"
                            }
                        }
                    }
                }
            }
        }
    }
}