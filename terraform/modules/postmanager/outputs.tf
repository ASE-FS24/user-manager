# output "cloudfront_usermanager_domain_name" {
#   value = aws_cloudfront_distribution.usermanager-distribution.domain_name
# }

output "postmanager_lambda_api_endpoint" {
  value = aws_apigatewayv2_stage.pm_default_api.invoke_url
}

output "postmanager_lambda_api_arn" {
  value = aws_lambda_function.postmanager_api.arn
}