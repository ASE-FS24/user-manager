########## FRONT END ############
resource "aws_s3_bucket" "frontend" {
  bucket        = "${var.fr_project_name}-frontend"
  force_destroy = "false"
  # region        = "${var.aws_region}"
  # website {
  #   index_document = "index.html"
  #   # error_document = "index.html"
  # }
}

resource "aws_s3_bucket_website_configuration" "frontend-config" {
  bucket = aws_s3_bucket.frontend.id
  index_document {
    suffix = "index.html"
  }
}

locals {
    mime_types = {
      ".html" = "text/html"
      ".png"  = "image/png"
      ".jpg"  = "image/jpeg"
      ".gif"  = "image/gif"
      ".css"  = "text/css"
      ".js"   = "application/javascript"
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

resource "aws_s3_object" "build" {
  for_each = fileset(var.build_folder, "**/*")
  bucket = aws_s3_bucket.frontend.bucket
  key    = each.value
  source = "${var.build_folder}/${each.value}"
  etag = filemd5("${var.build_folder}/${each.value}")
  acl = "public-read"
content_type = lookup(local.mime_types, regex("\\.[^.]+$", each.key), null)  
}


resource "aws_s3_bucket_policy" "frontend_policy" {
  bucket = aws_s3_bucket.frontend.id

  policy = <<EOF
    {
      "Version": "2012-10-17",
      "Statement": [
        {
          "Effect": "Allow",
          "Principal": "*",
          "Action": ["s3:GetObject"],
          "Resource": "arn:aws:s3:::${aws_s3_bucket.frontend.id}/*"
        }
      ]
    }
    EOF
}