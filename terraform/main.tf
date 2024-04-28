provider "aws" {
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
  region     = var.aws_region
}

module "usermanager" {
  source  = "./modules/usermanager"
  um_access_key = var.aws_access_key
  um_secret_key = var.aws_secret_key
}

module "postmanager" {
  source  = "./modules/postmanager"
  pm_access_key = var.aws_access_key
  pm_secret_key = var.aws_secret_key
}

module "frontend" {
  source  = "./modules/frontend"
  depends_on = [ module.usermanager, module.postmanager ]
  fr_access_key = var.aws_access_key
  fr_secret_key = var.aws_secret_key

}

# module "frontend" {
#   source = "./modules/frontend"
#   depends_on = [module.part_1]
# }