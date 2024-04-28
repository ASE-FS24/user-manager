 variable "build_folder" {
   default = "../../user-manager/build"
}

variable "fr_project_name" {
  type    = string
  default = "nexus-net"
}

variable "fr_deployment_number" {
  type    = string
  default = "0.0.1"
}

variable "fr_aws_region" {
  type = string
  default = "us-east-1"  #eu-central-1
}
