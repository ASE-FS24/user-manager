variable "pm_project_name" {
  type    = string
  default = "nexus-net"
}

variable "pm_deployment_number" {
  type    = string
  default = "0.0.1"
}

variable "pm_aws_region" {
  type = string
  default = "us-east-1"  #eu-central-1
}

variable "postmanager_name" {
   default = "postmanager"
}

variable "lambda_function_handler" {
  default = "ch.nexusnet.postmanager.aws.LambdaHandler::handleRequest"
}

variable "lambda_runtime" {
  default = "java17"
}

variable "api_path" {
  default = "{proxy+}"
}

variable "postmanager_jar" {
  default = "original-postmanager.jar"  
}