variable "um_project_name" {
  type    = string
  default = "nexus-net"
}

variable "um_deployment_number" {
  type    = string
  default = "0.0.1"
}

variable "um_aws_region" {
  type = string
  default = "us-east-1"  #eu-central-1
}

variable "usermanager_name" {
   default = "usermanager"
}

variable "lambda_function_handler" {
  default = "ch.nexusnet.usermanager.aws.LambdaHandler::handleRequest"
}

variable "lambda_runtime" {
  default = "java17"
}

variable "api_path" {
  default = "{proxy+}"
}

variable "usermanager_jar" {
  default = "original2-usermanager.jar"  
}

variable "build_folder" {
   default = "../../user-manager/build"
}