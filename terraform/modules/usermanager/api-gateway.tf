resource "aws_apigatewayv2_api" "usermanager" {
  name          = "${var.usermanager_name}-api"
  protocol_type = "HTTP"

  # Optionally, you can define CORS settings, etc. here
}

resource "aws_apigatewayv2_stage" "default_api" {
  api_id     = aws_apigatewayv2_api.usermanager.id
  name       = "users"
  auto_deploy = true

  default_route_settings {
    throttling_burst_limit = 10
    throttling_rate_limit  = 10
  }
}

#set permissions
resource "aws_apigatewayv2_integration" "usermanager_rest_api" {
  api_id           = aws_apigatewayv2_api.usermanager.id
  integration_type = "AWS_PROXY"
  integration_uri  = aws_lambda_function.usermanager_api.invoke_arn
}

resource "aws_lambda_permission" "rest_api" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.usermanager_api.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn = "${aws_apigatewayv2_api.usermanager.execution_arn}/*/*"
}

# # Define each path separately
# resource "aws_apigatewayv2_route" "create_user" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "POST /users"

#   target = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "get_user_by_id" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "GET /users/id/{userId}"

#   target = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }





# ############ USER MANAGER NEW ###############
# resource "aws_apigatewayv2_api" "usermanager" {
#   name          = "${var.usermanager_name}-api"
#   protocol_type = "HTTP"

#  # CORS configuration
#   cors_configuration {
#     allow_origins = ["*"]
#     allow_methods = ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
#     allow_headers = ["*"]
#   }
# }

# resource "aws_apigatewayv2_stage" "default_api" {
#   api_id     = aws_apigatewayv2_api.usermanager.id
#   name       = "user"
#   auto_deploy = true

#   default_route_settings {
#     throttling_burst_limit = 10
#     throttling_rate_limit  = 10
#   }
# }

# #set permissions
# resource "aws_apigatewayv2_integration" "usermanager_rest_api" {
#   api_id           = aws_apigatewayv2_api.usermanager.id
#   integration_type = "AWS_PROXY"
#   integration_uri  = aws_lambda_function.usermanager_api.invoke_arn
# }

# resource "aws_lambda_permission" "rest_api" {
#   statement_id  = "AllowAPIGatewayInvoke"
#   action        = "lambda:InvokeFunction"
#   function_name = aws_lambda_function.usermanager_api.function_name
#   principal     = "apigateway.amazonaws.com"

#   source_arn = "${aws_apigatewayv2_api.usermanager.execution_arn}/*/*"
# }

# # Define each path separately
# resource "aws_apigatewayv2_route" "create_user" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "POST /users"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "get_user_by_id" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "GET /users/id/{userId}"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "update_user" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "PUT /users/id/{userId}"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "delete_user" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "DELETE /users/id/{userId}"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "get_resume" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "GET /users/{userId}/resume"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "upload_resume" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "POST /users/{userId}/resume"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "get_profile_picture" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "GET /users/{userId}/profilePicture"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "upload_profile_picture" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "POST /users/{userId}/profilePicture"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }

# resource "aws_apigatewayv2_route" "get_user_by_username" {
#   api_id    = aws_apigatewayv2_api.usermanager.id
#   route_key = "GET /users/username/{username}"
#   target    = "integrations/${aws_apigatewayv2_integration.usermanager_rest_api.id}"
# }
