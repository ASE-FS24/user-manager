provider "aws" {
  shared_credentials_files = ["~/.aws/credentials"]
  region = var.aws_region
}

module "usermanager" {
  source  = "./modules/usermanager"
}

module "postmanager" {
  source  = "./modules/postmanager"
  depends_on = [ module.usermanager ]
}

module "frontend" {
  source  = "./modules/frontend"
  depends_on = [ module.usermanager, module.postmanager ]
}

# module "frontend" {
#   source = "./modules/frontend"
#   depends_on = [module.part_1]
# }