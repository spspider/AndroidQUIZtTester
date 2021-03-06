pipeline {
agent none
stages {

        stage ('download scripts from git to master') {
            agent {
                label 'master' 
            }
            steps {
                //git credentialsId: '5d0cd3c8-e66a-4c44-b2e6-83b731e625ac', url: 'git@github.com:spspider/AndroidQUIZtTester.git'
                git credentialsId: 'private_key_github', url: 'git@github.com:spspider/AndroidQUIZtTester.git'
                sh 'ls'

            }
        }
        stage('create server via Terraform and install with Ansible'){
            agent {label 'master'}
               
            steps {
                
                sh "chmod +x -R ${env.WORKSPACE}"
                dir("run/terraform") {
                    sh 'echo $USER'
                    sh './terraform_bash_automatic_script.sh'
                }
            }

        }
        stage ('download from git to slave docker_aws_node') {
            agent {
                label 'docker_aws_node' 
            }
            steps {
               //local
               //git credentialsId: '6f100a00-3000-45db-a46a-11b3216bf2e8', url: 'git@github.com:spspider/AndroidQUIZtTester.git'
               //aws
               
                //git credentialsId: '5d0cd3c8-e66a-4c44-b2e6-83b731e625ac', url: 'git@github.com:spspider/AndroidQUIZtTester.git'
                git credentialsId: 'private_key_github', url: 'git@github.com:spspider/AndroidQUIZtTester.git'
                sh 'ls'
            }
        }
        stage('docker compile') {
            agent {
                label 'docker_aws_node' 
                //args '-u root:root'
            }
            
            // parameters {
            //     choice(name: "ENVIRONMENT", choices: "NINGUNO\nDEV\nPRO", description: "The environment to be compiled")
            //     string(name: "EMAILS", defaultValue: "", description: "e-mails to send the builds")
            //     booleanParam(name: "UI_TESTS", defaultValue: false, description: "Do you want to want to run UI tests")
            //     string(name: "TAG_NAME", defaultValue: "", description: "The tag name of your code")
            //     string(name: "TAG_MESSAGE", defaultValue: "", description: "The message of your tag")
            // }

        //     steps   {
        //     //git credentialsId: '6f100a00-3000-45db-a46a-11b3216bf2e8', url: 'git@github.com:spspider/AndroidQUIZtTester.git'
        //     //sh 'ls'
        //   }

       
            steps {
                sh 'docker -v'
                sh 'echo $USER'
                //sh 'if [[ "$(docker images -q myimage:android-build:6.0.1-30-29 2> /dev/null)" == "" ]]; then  echo \'admin\' | sudo -S docker build -f "$PWD/docker/dockerfile" -t android-build:6.0.1-30-29 .; fi;'
                sh 'echo \'admin\' | sudo -S docker build -f "$PWD/run/docker/dockerfile2" -t android-build:6.0.1-30-29 .'
            }
        }
        stage ('compile project'){
            agent {
                label 'docker_aws_node'
            }
            steps{
               //sh 'docker run --rm -v "$PWD":/home/gradle/ -w /home/gradle android-build:5.4.1-28-27 gradle assembleDebug'
               
               //sh 'echo \'admin\' | sudo -S docker run --rm -v "$PWD":/home/gradle/ -w /home/gradle android-build:6.0.1-30-29 gradle ./gradlew clean build -x lint -x test'
               sh 'echo \'admin\' | sudo -S docker run --rm -v "$PWD":/home/gradle/ -w /home/gradle android-build:6.0.1-30-29 gradle assembleDebug'
            }
        }
          stage ('sending builded apk back'){
            agent {
                label 'master'
            }
            steps{
               //sh 'docker run --rm -v "$PWD":/home/gradle/ -w /home/gradle android-build:5.4.1-28-27 gradle assembleDebug'
               
               //sh 'echo \'admin\' | sudo -S docker run --rm -v "$PWD":/home/gradle/ -w /home/gradle android-build:6.0.1-30-29 gradle ./gradlew clean build -x lint -x test'
               //sh 'echo \'admin\' | sudo -S docker run --rm --memory="1g" -v "$PWD":/home/gradle/ -w /home/gradle android-build:6.0.1-30-29 gradle assembleDebug'
               dir("${env.WORKSPACE}/run/terraform") {
                    sh "chmod +x -R ${env.WORKSPACE}"
                    sh 'echo $PWD'
                    sh 'echo $USER'
                    sh './terraform_destroy.sh send_back' //destroy stop
                          
                }
            }
        }
         stage ('publish to GIT'){
            agent {
                label 'master'
            }
            steps{
               dir("${env.WORKSPACE}/run") {
                    sh "chmod +x -R ${env.WORKSPACE}"
                    sh 'echo $PWD'
                    sh 'echo $USER'
                    sh './publish_to_git.sh'
                          
                }
            }
        }
         stage ('destroy all servers'){
            agent {
                label 'master'
            }
             steps{
               dir("${env.WORKSPACE}/run/terraform") {
                    sh "chmod +x -R ${env.WORKSPACE}"
                    sh 'echo $PWD'
                    sh 'echo $USER'
                    sh './terraform_destroy.sh stop' //send_back destroy stop
                         
                }
            }
        }

    }
}