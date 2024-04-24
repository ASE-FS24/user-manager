resource "aws_s3_bucket" "usermanager" {
  bucket        = var.project_name
  force_destroy = false
}

resource "aws_s3_bucket_ownership_controls" "usermanager" {
  bucket = aws_s3_bucket.usermanager.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

resource "aws_s3_bucket_acl" "lambda_bucket" {
  depends_on = [aws_s3_bucket_ownership_controls.usermanager]

  bucket = aws_s3_bucket.usermanager.id
  acl    = "private"
}

resource "aws_s3_bucket_public_access_block" "usermanager" {
  bucket = aws_s3_bucket.usermanager.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_object" "usermanager_jar" {
  bucket = aws_s3_bucket.usermanager.bucket
  key    = "${var.deployment_number}/usermanager.jar"
  source = "../../user-manager/target/usermanager.jar"
}