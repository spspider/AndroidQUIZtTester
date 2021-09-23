#!/bin/bash
terraform init
terraform apply -auto-approve

#ip="$(jq '[.resources[].instances[].attributes.public_dns] | sort[]' terraform.tfstate)"
ip="$(jq '[.resources[].instances[].attributes.public_dns]  | .[]'   terraform.tfstate)"
name="$(jq '[.resources[].instances[].attributes.tags.Name]  | .[]'   terraform.tfstate)"

IFS=$'\n' read -rd '' -a ip_arr <<<"$ip"
IFS=$'\n' read -rd '' -a name_arr <<<"$name"


folder_ansible='../ansible' filename="$folder_ansible/hosts.ini"; rm -rf $filename; mkdir $folder_ansible
echo '[server]' >> $filename
for ((i = 0; i < (${#ip_arr[@]} ); i++)); do
#for a in "${name_arr[@]}"; do 
# name_servers=$(echo "${array[$i]}" | cut -d ""\" -f 2)
ip_that=$(echo "${ip_arr[$i]}" | cut -d ""\" -f 2)
name_that=$(echo "${name_arr[$i]}" | cut -d ""\" -f 2 | sed 's/ //g')
echo  "$name_that" 'ansible_host='"$ip_that" >> $filename
done

echo '' >> $filename
echo '[same_cred:children]' >> $filename
echo 'server' >> $filename

#directory_group_vars='group_vars'
#mkdir "$folder_ansible/$directory_group_vars"

cat $filename


#-----------------------------now ansible turn


ansible-inventory -i $filename --list
#ip='[ "", "ec2-18-169-240-132.eu-west-2.compute.amazonaws.com" ]'

#
export ANSIBLE_HOST_KEY_CHECKING=False


ansible-playbook $folder_ansible/install_docker.yml -i $folder_ansible/hosts.ini --ssh-common-args='-o StrictHostKeyChecking=no' -v

#echo ip:$ip

#generate ini file for ansible


