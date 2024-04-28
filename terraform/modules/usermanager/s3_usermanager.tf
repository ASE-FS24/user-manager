########## USER MANAGER ############
resource "aws_s3_bucket" "usermanager" {
  bucket        = "${var.um_project_name}-${var.usermanager_name}"
  force_destroy = "false"
  # region        = "${var.aws_region}"
}

resource "aws_s3_bucket_ownership_controls" "usermanager_ownership" {
  bucket = aws_s3_bucket.usermanager.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

resource "aws_s3_bucket_acl" "usermanager_acl" {
  depends_on = [aws_s3_bucket_ownership_controls.usermanager_ownership]

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
  key    = "${var.um_deployment_number}/${var.usermanager_jar}"
  source = "../../user-manager/target/${var.usermanager_jar}"
}