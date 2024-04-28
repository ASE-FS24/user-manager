# # Define the CloudFront distribution for the user manager
# resource "aws_cloudfront_distribution" "usermanager-distribution" {
#   enabled             = true
#   price_class         = "PriceClass_All"
#   is_ipv6_enabled     = true
#   default_root_object = ""

#   # Define origins
#   origin {
#     domain_name = aws_lambda_function.usermanager_api.invoke_arn
#     origin_id   = "lambda-origin"

#     custom_origin_config {
#       http_port                = 80
#       https_port               = 443
#       origin_protocol_policy   = "https-only"
#       origin_ssl_protocols     = ["TLSv1.2"]
#       origin_keepalive_timeout = 5
#       origin_read_timeout      = 30
#     }
#   }

#   # Define default cache behavior
#   default_cache_behavior {
#     target_origin_id       = "lambda-origin"
#     viewer_protocol_policy = "redirect-to-https"
#     compress               = true
#     allowed_methods        = ["GET", "HEAD", "OPTIONS"]
#     cached_methods         = ["GET", "HEAD", "OPTIONS"]
#     forwarded_values {
#       query_string = true
#       cookies {
#         forward = "none"
#       }
#       headers = ["Origin", "Access-Control-Request-Headers", "Access-Control-Request-Method"]
#     }
#   }

#   # Configure restrictions if needed
#   restrictions {
#     geo_restriction {
#       # restriction_type = "none"
#       restriction_type = "whitelist"
#       locations        = ["CH"]
#     }
#   }

#   # Viewer certificate configuration if required
#   viewer_certificate {
#     cloudfront_default_certificate = true  # Use the default CloudFront certificate
#   }

# #   # Lambda@Edge function association
# #   lambda_function_association {
# #     event_type   = "viewer-request"
# #     lambda_arn   = aws_lambda_function.edge_lambda.qualified_arn
# #     include_body = false
# #   }

#   # Additional configurations can be added as needed
# }
