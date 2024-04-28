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
  default = "us-east-1"  #eu-central-1
}

variable "aws_access_key" {
  type        = string
  description = "Sensitive access key"
  sensitive   = true
}

variable "aws_secret_key" {
  type        = string
  description = "Sensitive secret key"
  sensitive   = true
}
# variable "lambda_function_handler" {
#   default = "ch.nexusnet.usermanager.aws.LambdaHandler::handleRequest"
# }

# variable "lambda_runtime" {
#   default = "java17"
# }

# variable "api_path" {
#   default = "{proxy+}"
# }

# variable "usermanager_jar" {
#   default = "original2-usermanager.jar"  
# }

# variable "build_folder" {
#    default = "../../user-manager/build"
# }

# variable "usermanager_name" {
#    default = "usermanager"
# }

# variable "postmanager_name" {
#    default = "postmanager"
# }

# variable "frontend_name" {
#   default = "frontend"
# }