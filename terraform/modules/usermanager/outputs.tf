# output "cloudfront_usermanager_domain_name" {
#   value = aws_cloudfront_distribution.usermanager-distribution.domain_name
# }

output "usermanager_lambda_api_endpoint" {
  value = aws_apigatewayv2_stage.default_api.invoke_url
}

output "dynamodb_endpoint_userinfo" {
  value = aws_dynamodb_table.user_info.arn
}

output "dynamodb_endpoint_follow" {
  value = aws_dynamodb_table.follow.arn
}

output "usermanager_lambda_api_arn" {
  value = aws_lambda_function.usermanager_api.arn
}