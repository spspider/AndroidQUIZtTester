#!/bin/bash


name="$(jq '[.resources[].instances[].attributes.tags.Name]  | .[]' terraform/terraform.tfstate)"

IFS=$'\n' read -rd '' -a name_arr <<<"$name"


for ((i = 0; i < (${#name_arr[@]}); i++)); do


    name_that=$(echo "${name_arr[$i]}" | cut -d ""\" -f 2 | sed 's/ //g')
    #----------------perfom secure copy
    pwd
    ls
    date_time=$(date +"%Y-%m-%d %T")
    path="output/$name_that/apk"
    echo ":airplane: tTesterUKDD from machine:$name_that at $date_time" >> "$path"/README.md\

    

    git -C "$path" init 
    git -C "$path" add .
    git -C "$path" commit -m "commit $name_that at $date_time" 
    git -C "$path" branch -M master  
    git -C "$path" remote rm origin
    git -C "$path" remote add origin git@github.com:spspider/tTesterUKDD.git  
    git -C "$path" push -f --set-upstream origin master

done

#rm -rf "$path"


#pwd "$(date +"%T")"
