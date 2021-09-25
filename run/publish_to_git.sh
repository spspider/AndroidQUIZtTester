#!/bin/bash


name="$(jq '[.resources[].instances[].attributes.tags.Name]  | .[]' terraform/terraform.tfstate)"

IFS=$'\n' read -rd '' -a name_arr <<<"$name"


for ((i = 0; i < (${#name_arr[@]}); i++)); do
    name_that=$(echo "${name_arr[$i]}" | cut -d ""\" -f 2 | sed 's/ //g')
    #----------------perfom secure copy
    echo "# tTesterUKDD from machine:$name_that" >> README.md
    path="output/$name_that/apk/debug/"
    date_time=$(date +"%Y-%m-%d %T")
    git -C "$path" init 
    git -C "$path" add 
    git -C "$path" commit -m "commit $date_time" 
    git -C "$path" branch -M master  
    git -C "$path" remote add origin git@github.com:spspider/tTesterUKDD.git  
    git -C "$path" push -u origin master 

done



#pwd "$(date +"%T")"
