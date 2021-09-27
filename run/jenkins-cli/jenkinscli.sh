#!/bin/bash
 
#JENKINS_URL='http://ec2-35-178-176-67.eu-west-2.compute.amazonaws.com:8080'
#java -jar jenkins-cli.jar -s http://3.10.164.251:8080/ -webSocket help
#java -jar ../jenkins-cli/jenkins-cli.jar -auth 'serhii:admin' -s http://ec2-18-130-56-20.eu-west-2.compute.amazonaws.com:8080 get-node docker_aws_node
#get-node
JENKINS_URL='http://'$2':8080'
"pwd"
JAVA_file='../jenkins-cli/jenkins-cli.jar'
NODE_NAME=$4
NODE_HOME='/home/ubuntu/jenkins'
AUTH='serhii:admin'
EXECUTORS=1
SSH_PORT=22
#HOST='ec2-18-132-114-135.eu-west-2.compute.amazonaws.com'
HOST="$3"

LABELS=build
USERID=${USER}
 
function startbuild(){
id_that='i-0ab25359d2c58d000'
../aws/bash_aws.sh 'check' "$id_that"
ip_jenkins=$(../aws/bash_aws.sh 'get_URL' "$id_that")
local JENKINS_URL="http://$ip_jenkins:8080"
local JAVA_file='jenkins-cli.jar'
java -jar $JAVA_file -auth $AUTH -s "$JENKINS_URL" build 'ProjectAndroidPipeline' -v -f -s
#java -jar $JAVA_file -auth $AUTH -s "$JENKINS_URL" build "$NODE_NAME"  

}

function connectnode(){
java -jar $JAVA_file -auth $AUTH -s "$JENKINS_URL" connect-node "$NODE_NAME"   
}
#../jenkins-cli/jenkinscli.sh 'connect' 'http://ec2-3-9-132-94.eu-west-2.compute.amazonaws.com:8080/' 'host' 'docker_aws_node'

function change_slave_docker_aws(){
echo "delete node $NODE_NAME"
java -jar $JAVA_file -auth $AUTH -s "$JENKINS_URL" delete-node "$NODE_NAME"
echo "create node $NODE_NAME"
cat <<EOF | java -jar $JAVA_file -auth $AUTH -s $JENKINS_URL create-node $NODE_NAME

<slave>
  <name>docker_aws_node</name>
  <description></description>
  <remoteFS>/home/ubuntu/jenkins</remoteFS>
  <numExecutors>1</numExecutors>
  <mode>NORMAL</mode>
  <retentionStrategy class="hudson.slaves.RetentionStrategy$Always"/>  <launcher class="hudson.plugins.sshslaves.SSHLauncher" plugin="ssh-slaves@1.33.0">
    <host>$HOST</host>   
    <port>22</port>
    <credentialsId>androidnode</credentialsId>
    <launchTimeoutSeconds>60</launchTimeoutSeconds>
    <maxNumRetries>10</maxNumRetries>
    <retryWaitTime>15</retryWaitTime>
    <sshHostKeyVerificationStrategy class="hudson.plugins.sshslaves.verifiers.NonVerifyingKeyVerificationStrategy"/>
    <tcpNoDelay>true</tcpNoDelay>
  </launcher>
  <label>docker_aws_node node</label>
  <nodeProperties/>
</slave>
EOF

}
function change_node(){
#java -jar /project/jenkins-cli/jenkins-cli.jar -auth serhii:admin -s 'http://ec2-18-130-50-96.eu-west-2.compute.amazonaws.com:8080' get-node docker_aws_node
echo "delete node $NODE_NAME"
java -jar $JAVA_file -auth $AUTH -s $JENKINS_URL delete-node $NODE_NAME
echo "create node $NODE_NAME"
cat <<EOF | java -jar $JAVA_file -auth $AUTH -s $JENKINS_URL create-node $NODE_NAME
<slave>
  <name>$NODE_NAME</name>
  <description></description>
  <remoteFS>$NODE_HOME</remoteFS>
  <numExecutors>1</numExecutors>
  <mode>NORMAL</mode>
  <retentionStrategy class="hudson.slaves.RetentionStrategy$Always"/>
  <launcher class="hudson.plugins.sshslaves.SSHLauncher" plugin="ssh-slaves@1.33.0">
    <host>$HOST</host>
    <port>22</port>
    <credentialsId>androidnode</credentialsId>
    <launchTimeoutSeconds>60</launchTimeoutSeconds>
    <maxNumRetries>10</maxNumRetries>
    <retryWaitTime>15</retryWaitTime>
    <sshHostKeyVerificationStrategy class="hudson.plugins.sshslaves.verifiers.ManuallyTrustedKeyVerificationStrategy">
      <requireInitialManualTrust>false</requireInitialManualTrust>
    </sshHostKeyVerificationStrategy>
    <tcpNoDelay>true</tcpNoDelay>
  </launcher>
  <label>ubuntu android</label>
  <nodeProperties>
    <hudson.slaves.EnvironmentVariablesNodeProperty>
      <envVars serialization="custom">
        <unserializable-parents/>
        <tree-map>
          <default>
            <comparator class="hudson.util.CaseInsensitiveComparator"/>
          </default>
          <int>1</int>
          <string>ANDROID_HOME</string>
          <string>/home/ubuntu/android-sdk</string>
        </tree-map>
      </envVars>
    </hudson.slaves.EnvironmentVariablesNodeProperty>
  </nodeProperties>
</slave>
EOF
echo "connect node $NODE_NAME"
java -jar $JAVA_file -auth $AUTH -s $JENKINS_URL connect-node $NODE_NAME
}

function test_conn(){
echo $JENKINS_URL
attempt_counter=0
max_attempts=5

#until $(curl --output /dev/null --silent --fail -r 0-0 "$JENKINS_URL":8080); do
until $(wget --spider "$JENKINS_URL":8080 2>/dev/null); do
    if [ ${attempt_counter} -eq ${max_attempts} ];then
      echo "Max attempts reached"
      exit 1
    fi

    printf '.'
    attempt_counter=$(($attempt_counter+1))
    sleep 5
done

  #java -jar $JAVA_file -auth $AUTH -s $JENKINS_URL -webSocket help
}

function show_help(){
echo ""
echo '$1 - name'
echo '$2 - JENKINS_URL=http://$2:8080'
echo '$3 - HOST=$3'
echo '$4 - NODE_NAME'
}

if [ "$1" = 'testCon' ];then
  test_conn
elif [ "$1" = 'docker' ];then
  change_slave_docker_aws
elif [ "$1" = 'connect' ];then
  connectnode
elif [ "$1" = 'start' ];then
  startbuild
else
show_help
fi