variable "aws_access_key" {
  # set aws access key
  default = ""
}

variable "aws_secret_key" {
  # set aws secret key
  default = ""
}

variable "region" {
  # set aws region
  default = "eu-central-1"
}

variable "lambda_payload_filename" {
  default = "./target/usermanager.jar"
}

variable "lambda_function_handler" {
  default = "ch.nexusnet.usermanager.LambdaHandler::handleRequest"
}

variable "lambda_runtime" {
  default = "java17"
}

variable "api_path" {
  default = "{proxy+}"
}

variable "project_name" {
  type    = string
  default = "nexus-net"
}

variable "deployment_number" {
  type    = string
  default = "0.0.1"
}

variable "aws_region" {
  description = "AWS region for all resources."

  type    = string
  default = "eu-central-1"
}

variable "api_env_stage_name" {
  default = "terraform-lambda-java-stage"
}