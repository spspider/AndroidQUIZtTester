#!/bin/bash

id="$(jq '[.resources[].instances[].attributes.id]  | .[]' terraform.tfstate)"
name="$(jq '[.resources[].instances[].attributes.tags.Name]  | .[]' terraform.tfstate)"

IFS=$'\n' read -rd '' -a id_arr <<<"$id"
IFS=$'\n' read -rd '' -a name_arr <<<"$name"

for ((i = 0; i < (${#id_arr[@]}); i++)); do
    id_that=$(echo "${id_arr[$i]}" | cut -d ""\" -f 2)
    ip_that2=$(../aws/bash_aws.sh 'get_URL' "$id_that")
    name_that=$(echo "${name_arr[$i]}" | cut -d ""\" -f 2 | sed 's/ //g')

    #----------------perfom secure copy
    mkdir -p ../output/"$name_that"
    scp -r -i ~/.ssh/keypair_jenkins.pem ubuntu@"$ip_that2":~/jenkins/workspace/ProjectAndroidPipeline/app/build/outputs/apk ../output/"$name_that"
    #scp ubuntu@remote:/file/to/send /where/to/put

    #----------------------------------


done

#terraform destroy