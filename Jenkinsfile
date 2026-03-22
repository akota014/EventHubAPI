pipeline {
    agent any
    
    tools{
		git 'Git'
	}

    stages {
        stage('Clone repo') {
            steps {
                // Get some code from a GitHub repository
                git branch:'main', url:'https://github.com/jglick/simple-maven-project-with-tests.git'
            }
        }
        
        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }
        
	}
  }
