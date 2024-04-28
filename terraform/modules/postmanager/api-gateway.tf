############ POST MANAGER ###############
resource "aws_apigatewayv2_api" "postmanager" {
  name          = "${var.postmanager_name}-api"
  protocol_type = "HTTP"

  # CORS configuration
  cors_configuration {
    allow_origins = ["*"]
    allow_methods = ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
    allow_headers = ["*"]
  }
}

resource "aws_apigatewayv2_stage" "pm_default_api" {
  api_id     = aws_apigatewayv2_api.postmanager.id
  name       = "post"
  auto_deploy = true

  default_route_settings {
    throttling_burst_limit = 10
    throttling_rate_limit  = 10
  }
}

# Set permissions
resource "aws_apigatewayv2_integration" "postmanager_rest_api" {
  api_id           = aws_apigatewayv2_api.postmanager.id
  integration_type = "AWS_PROXY"
  integration_uri  = aws_lambda_function.postmanager_api.invoke_arn
}

resource "aws_lambda_permission" "rest_api" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.postmanager_api.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_apigatewayv2_api.postmanager.execution_arn}/*/*"
}

# Define each path separately
resource "aws_apigatewayv2_route" "create_post" {
  api_id    = aws_apigatewayv2_api.postmanager.id
  route_key = "POST /posts"

  target = "integrations/${aws_apigatewayv2_integration.postmanager_rest_api.id}"
}

resource "aws_apigatewayv2_route" "get_all_posts" {
  api_id    = aws_apigatewayv2_api.postmanager.id
  route_key = "GET /posts"

  target = "integrations/${aws_apigatewayv2_integration.postmanager_rest_api.id}"
}

resource "aws_apigatewayv2_route" "get_post_by_id" {
  api_id    = aws_apigatewayv2_api.postmanager.id
  route_key = "GET /posts/{id}"

  target = "integrations/${aws_apigatewayv2_integration.postmanager_rest_api.id}"
}

resource "aws_apigatewayv2_route" "get_posts_by_author_id" {
  api_id    = aws_apigatewayv2_api.postmanager.id
  route_key = "GET /posts/user/{authorId}"

  target = "integrations/${aws_apigatewayv2_integration.postmanager_rest_api.id}"
}

resource "aws_apigatewayv2_route" "update_post" {
  api_id    = aws_apigatewayv2_api.postmanager.id
  route_key = "PUT /posts/{id}"

  target = "integrations/${aws_apigatewayv2_integration.postmanager_rest_api.id}"
}

resource "aws_apigatewayv2_route" "delete_post" {
  api_id    = aws_apigatewayv2_api.postmanager.id
  route_key = "DELETE /posts/{id}"
  target = "integrations/${aws_apigatewayv2_integration.postmanager_rest_api.id}"
}
