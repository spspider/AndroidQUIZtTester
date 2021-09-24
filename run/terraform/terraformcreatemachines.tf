terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.27"
    }
  }

  required_version = ">= 0.14.9"
}

provider "aws" {
  profile = "default"
  # access_key = var.AWS_ACCESS_KEY
  # secret_key = var.AWS_SECRET_KEY
  region  = "eu-west-2"
}

resource "aws_instance" "app_server" {
  #count = 1
  ami           = "ami-0194c3e07668a7e36" # ubuntu 64
  instance_type = "t2.medium"
  key_name      = "keypair_jenkins"
  tags = {
    #Name = "docker_terraform${count.index}"
    Name = "docker_terraform"
  }
  security_groups = ["launch-wizard-11", ]
  user_data       = <<EOF
  #! /bin/bash
  sudo apt-get update
  sudo apt update -y
  sudo apt install mc
  sudo apt-get -qq install python -y
  EOF
  
 #jq '[.resources[].instances[].attributes.public_dns] | sort[]' terraform.tfstate | haform.tfstate | head
  # provisioner "remote-exec" {
  #   inline = ["sudo apt-get -qq install python -y"]
  # }

  # connection {
  # 	type        = "ssh"
  # 	user        = "ubuntu"
  # 	password    = "${rsadecrypt(aws_instance.app_server.password_data, file("keypair_jenkins.pem"))}"
  # 	host        = "${aws_instance.app_server.public_ip}"
  # }

}


# resource "local_file" "ec2_iini" {
#   filename = "ec2.ini"
#   content = <<-EOT
#     %{ for ip in aws_instance.app_server.*.public_ip ~}
#     ${ip} ansible_ssh_user=ec2-user
#     %{ endfor ~}
#   EOT
# }

output "server_public_ip" {
  value = aws_instance.app_server[*].public_ip
  #value >> file

}

