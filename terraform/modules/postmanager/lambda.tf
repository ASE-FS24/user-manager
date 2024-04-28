resource "aws_lambda_function" "postmanager_api" {
  depends_on = [ aws_s3_bucket.postmanager]
  function_name = "postmanager_api"
  environment {
    variables = {
      AMAZON_ACCESS_KEY	= "${var.pm_access_key}"
      AMAZON_DYNAMODB_ENDPOINT = 	"https://dynamodb.${var.pm_aws_region}.amazonaws.com"
      AMAZON_DYNAMODB_ACCESS_KEY	= "${var.pm_access_key}"
      AMAZON_DYNAMODB_SECRET_KEY	= "${var.pm_secret_key}"
      AMAZON_S3_ACCESS_KEY	= "${var.pm_access_key}"
      AMAZON_S3_ENDPOINT	= "https://${var.pm_project_name}-${var.postmanager_name}.s3.${var.pm_aws_region}.amazonaws.com/0.0.1/"
      AMAZON_S3_SECRET_KEY	= "${var.pm_secret_key}"
      AMAZON_SECRET_KEY	= "${var.pm_secret_key}"
      AMAZON_USM_BUCKET	= "nexus-net-${var.postmanager_name}"
    }
  }

  s3_bucket = aws_s3_bucket.postmanager.bucket
  s3_key    = "${var.pm_deployment_number}/${var.postmanager_jar}"
  handler          = var.lambda_function_handler
  timeout          = 60
  memory_size      = 256
  runtime = var.lambda_runtime

  role = aws_iam_role.pm_lambda_rest_api.arn
  publish = true
}

resource "aws_iam_role" "pm_lambda_rest_api" {
  name = "pm_lambda_rest_api"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

# resource "aws_lambda_permission" "usermanager_cloudfront" {
#   depends_on = [ aws_cloudfront_distribution.usermanager-distribution ]
#   action        = "lambda:InvokeFunction"
#   function_name = aws_lambda_function.usermanager_api.function_name
#   principal     = "cloudfront.amazonaws.com"
#   source_arn    = aws_cloudfront_distribution.usermanager-distribution.arn
# }
