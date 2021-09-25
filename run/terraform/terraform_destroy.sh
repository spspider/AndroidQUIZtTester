#!/bin/bash

id="$(jq '[.resources[].instances[].attributes.id]  | .[]' terraform.tfstate)"
name="$(jq '[.resources[].instances[].attributes.tags.Name]  | .[]' terraform.tfstate)"

IFS=$'\n' read -rd '' -a id_arr <<<"$id"
IFS=$'\n' read -rd '' -a name_arr <<<"$name"

copy_from=~/jenkins/workspace/ProjectAndroidPipeline/app/build/outputs/apk
copy_to=../output/"$name_that"

id_arr_=()

for ((i = 0; i < (${#id_arr[@]}); i++)); do
    id_that=$(echo "${id_arr[$i]}" | cut -d ""\" -f 2)
    ip_that2=$(../aws/bash_aws.sh 'get_URL' "$id_that")
    name_that=$(echo "${name_arr[$i]}" | cut -d ""\" -f 2 | sed 's/ //g')
    id_arr_[$i]+=$id_that

    #----------------perfom secure copy
    mkdir -p ../output/"$name_that"
    echo "copy from:ubuntu@$ip_that2:$copy_from to $copy_to"
    scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -v -i ~/.ssh/keypair_jenkins.pem ubuntu@"$ip_that2":$copy_from "$copy_to"

    #scp ubuntu@remote:/file/to/send /where/to/put

    #----------------------------------
done
#sudo scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -i ~/.ssh/keypair_jenkins.pem ubuntu@3.10.141.98:/var/lib/jenkins/workspace/ProjectAndroidPipeline/run/output/docker_terraform/apk /var/lib/jenkins/workspace/ProjectAndroidPipeline/run/output/docker_terraform/apk
function dostop() {
    echo '#############################################'
    for ((i = 0; i < (${#id_arr_[@]}); i++)); do
        ../aws/bash_aws.sh stop "${id_arr_[$i]}"
    done
}

if [ "$1" = 'destroy' ]; then
    terraform destroy -auto-approve
elif [ "$1" = 'stop' ]; then
    dostop
fi
#сделать на выбор destroy stop
#terraform destroy
