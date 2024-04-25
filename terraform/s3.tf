########## FRONT END ############
resource "aws_s3_bucket" "frontend" {
  bucket        = "${var.project_name}-frontend"
  force_destroy = "false"
  website {
    index_document = "index.html"
    error_document = "error.html"
  }
}

resource "aws_s3_bucket_ownership_controls" "frontend_ownership" {
  bucket = aws_s3_bucket.frontend.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

resource "aws_s3_bucket_acl" "frontend_acl" {
  depends_on = [aws_s3_bucket_ownership_controls.frontend_ownership]

  bucket = aws_s3_bucket.frontend.id
  acl    = "public-read"
}

resource "aws_s3_bucket_public_access_block" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_object" "frontend_files" {
  for_each = fileset(var.build_folder, "**/*")

  bucket = aws_s3_bucket.frontend.bucket
  key    = each.key
  source = "${var.build_folder}/${each.key}"

  acl = "public-read"
}


########## USER MANAGER ############
resource "aws_s3_bucket" "backend" {
  bucket        = "${var.project_name}-backend"
  force_destroy = "false"
}

resource "aws_s3_bucket_ownership_controls" "backend_ownership" {
  bucket = aws_s3_bucket.backend.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

resource "aws_s3_bucket_acl" "backend_acl" {
  depends_on = [aws_s3_bucket_ownership_controls.backend_ownership]

  bucket = aws_s3_bucket.frontend.id
  acl    = "private"
}
resource "aws_s3_bucket_public_access_block" "backend" {
  bucket = aws_s3_bucket.backend.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_object" "usermanager_jar" {
  bucket = aws_s3_bucket.backend.bucket
  key    = "${var.deployment_number}/${var.usermanager_jar}"
  source = "../../user-manager/target/${var.usermanager_jar}"
}