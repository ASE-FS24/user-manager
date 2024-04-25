variable "project_name" {
  type    = string
  default = "nexus-net"
}

variable "deployment_number" {
  type    = string
  default = "0.0.1"
}

variable "aws_region" {
  type = string
  default = "eu-central-1"
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
  default = "original-usermanager.jar"  
}

variable "build_folder" {
   default = "../../user-manager/build"
}