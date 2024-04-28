resource "aws_lambda_function" "usermanager_api" {
  depends_on = [ aws_s3_bucket.usermanager]
  function_name = "usermanager_api"
  environment {
    variables = {
      AMAZON_ACCESS_KEY	= "AKIA2UC3FQUXL2TAFQHT"
      AMAZON_DYNAMODB_ENDPOINT = 	"https://dynamodb.${var.um_aws_region}.amazonaws.com"
      AMAZON_S3_ACCESS_KEY	= "AKIA2UC3FQUXL2TAFQHT"
      AMAZON_S3_ENDPOINT	= "https://${var.um_project_name}-${var.usermanager_name}.s3.${var.um_aws_region}.amazonaws.com/0.0.1/"
      AMAZON_S3_SECRET_KEY	= "l66He4T6aepctykh2JQL4yB91A2M/iE54WQDaEN4"
      AMAZON_SECRET_KEY	= "l66He4T6aepctykh2JQL4yB91A2M/iE54WQDaEN4"
      AMAZON_USM_BUCKET	= "nexus-net-${var.usermanager_name}"
    }
  }

  s3_bucket = aws_s3_bucket.usermanager.bucket
  s3_key    = "${var.um_deployment_number}/${var.usermanager_jar}"
  handler          = var.lambda_function_handler
  timeout          = 60
  memory_size      = 256
  runtime = var.lambda_runtime

  role = aws_iam_role.lambda_rest_api.arn
  publish = true
}

resource "aws_iam_role" "lambda_rest_api" {
  name = "lambda_rest_api"
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

data "aws_iam_policy" "AWSLambdaBasicExecutionRole" {
  arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_iam_role_policy_attachment" "AWSLambdaBasicExecutionRole_to_lambda_rest_api" {
  policy_arn = data.aws_iam_policy.AWSLambdaBasicExecutionRole.arn
  role = aws_iam_role.lambda_rest_api.name
}


resource "aws_iam_policy" "dynamodb_default" {
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "dynamodb:PutItem",
                "dynamodb:GetItem",
                "dynamodb:Query",
                "dynamodb:GetRecords",
                "dynamodb:Scan"
            ],
            "Resource": [
                "${aws_dynamodb_table.user_info.arn}",
                "${aws_dynamodb_table.follow.arn}",
                "${aws_dynamodb_table.posts.arn}"
            ]
        }
    ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "dynamodb_default_to_lambda_rest_api" {
  policy_arn = aws_iam_policy.dynamodb_default.arn
  role = aws_iam_role.lambda_rest_api.name
}