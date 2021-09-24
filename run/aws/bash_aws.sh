#!/bin/bash

function start_server() {
    echo "$(aws ec2 start-instances --instance-ids "$1")"
    check_runnig_instance "$1"
    echo "$(get_URL $1)"
}

function stop_server() {
    echo "$(aws ec2 stop-instances --instance-ids "$1")"
}
function get_URL {
    local returned_url="$(aws ec2 describe-instances --instance-ids "$1" --query 'Reservations[0].Instances[0].PublicDnsName')"

    # until [ ${#returned_url} = 2 ]; do
    #     echo "server $1 is loading... (no connection)"
    #     get_URL "$1"
    #     printf '.'
    #     sleep 10
    # done
    # if [ ${#returned_url} = 2 ]; then
    # echo "returned:$returned_url"
    #     get_URL "$1"
    #     echo "server is loading..."
    #     return
    # fi
    echo "$returned_url" | cut -d ""\" -f 2
}

function check_runnig_instance() {
    if [ -z "$name_servers" ];then
    name_servers="$1"
    fi
    name_code_run=$(aws ec2 describe-instances --instance-ids "$name_servers" --query 'Reservations[].Instances[].State[].Name')

    name_code=$(echo "$name_code_run" | awk -F'[][]' 'NR==2{print $1}' | cut -d ""\" -f 2) #print only 2nd field and cut ""
    #echo $name_servers' is:'$name_code
    if [ "$name_code" = 'stopped' ]; then
        echo "stop"
    fi
    if [ "$name_code" != 'running' ]; then
        printf '#'
        check_runnig_instance "$name_servers"
    fi
    #aws ec2 describe-instances --instance-ids "$name_servers" --query 'Reservations[].Instances[].State[].Code'
}
function check_until_server_become_online() {
    echo "please wait until server $1 become online"
    until $(curl --output /dev/null --silent --head --fail http://"$1"); do
        printf '.'
        echo "$(date +"%T")"
        sleep 5
    done

    # echo "server online, standby 10 sec for loading"
    # sleep 10
}

function test_server() {

    printf "Waiting for $1"
    until nc -z "$1" "$2" 2>/dev/null; do
        printf '.'
        sleep 10
    done
    echo
    echo "server is up! delay for loading jenkins..."
    test_jenkins_is_ready
    #sleep 60

}

function get_key_name() {
    string=$(aws ec2 describe-instances --query "Reservations[].Instances[].Tags[].Value")
    IFS=$'\n' read -rd '' -a array <<<"$string"
    echo 'numbers:'${#array[@]}
    for ((i = 1; i < ((${#array[@]} - 1)); i++)); do
        name_servers=$(echo "${array[$i]}" | cut -d ""\" -f 2)
        echo "$name_servers"
        # if [ "$1" -eq 1 ]; then
        #     start_server "$name_servers"
        # elif [ "$1" -eq 2 ]; then
        #     stop_server "$name_servers"
        # fi
    done
}
function all_server() {
    string=$(aws ec2 describe-instances --query "Reservations[].Instances[].InstanceId")
    #string='[ "i-0ab25359d2c58d000", "i-02ded942f1da932c6", "i-08e4a58cb94a0251d", "i-086bc4b4710a0ba53" ]'

    #printf "string%s" "$string"
    IFS=$'\n' read -rd '' -a array <<<"$string"
    echo 'numbers:'${#array[@]}
    #for element in "${array[@]}"; do
    for ((i = 1; i < ((${#array[@]} - 1)); i++)); do
        #if [[ "$i" -gt 0 ]] && [ "$i" -lt "${#array[@]}" ]; then
        name_servers=$(echo "${array[$i]}" | cut -d ""\" -f 2)
        if [ "$1" -eq 1 ]; then
            start_server "$name_servers"
        elif [ "$1" -eq 2 ]; then
            stop_server "$name_servers"
        elif [ "$1" -eq 3 ]; then
            check_runnig_instance "$name_servers"
        fi
    done
}
function start_jenkins_and_node() {
    jenk='i-0ab25359d2c58d000'
    node='i-08e4a58cb94a0251d'
    start_server "$jenk"
    start_server "$node"
    #-------------------------maybe after that need to wait

    #check_runnig_instance "$jenk"
    #check_runnig_instance "$node"

    jenkins_url=$(get_URL "$jenk")
    echo "$jenkins_url"
    android_url=$(get_URL "$node")
    echo "$android_url"

    test_server "$jenkins_url" "8080"
    #check_until_server_become_online "$jenkins_url"':8080'
    #check_until_server_become_online "$android_url"

    ../jenkins-cli/jenkinscli.sh 'change' $jenkins_url $android_url
}
function test_func() {
    check_until_server_become_online 'i-0ab25359d2c58d000'
}
function command_not_recognized() {
    echo '-----------------------'
    echo "command not recognized"
    echo "USAGE:"
    echo 'getName - show name of all instances'
    echo 'stopALL - stop all'
    echo 'startALL - start all'
    echo 'checkALL - check if it run'
    echo 'get_URL - get url $2 of machine'
    echo 'start - start $2 of machine'
    echo 'jenkAndNode - jenkins and slave'
    echo 'test - for test purpose'
    echo '-----------------------'
    exit 255
}
if [ -z "$1" ]; then
    command_not_recognized
#fi
elif [ "$1" = 'startALL' ]; then
    all_server 1
elif [ "$1" = 'stopALL' ]; then
    all_server 2
    echo "All servers has stopped, stanby or check status with checkALL"
elif [ "$1" = 'checkALL' ]; then
    all_server 3
elif [ "$1" = 'getName' ]; then
    get_key_name
elif [ "$1" = 'jenkAndNode' ]; then
    start_jenkins_and_node
elif [ "$1" = 'start' ]; then
    if [ -z "$2" ]; then
    #no server, retrive server list
        all_server 3
    else
    start_server "$2"
    fi
elif [ "$1" = 'start1' ]; then    
./bash_aws.sh start 'i-0ab25359d2c58d000'
elif [ "$1" = 'get_URL' ]; then
    get_URL "$2"
elif [ "$1" = 'test' ]; then
    check_runnig_instance 'i-0ab25359d2c58d000'
elif [ "$1" = 'check' ]; then
    name_servers="$2"
    check_runnig_instance "$name_servers"
else
    command_not_recognized
    #if [ "$1" = 'start ALL' ]; then

fi
