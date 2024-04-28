# resource "aws_route53_zone" "nexusnet" {
#   count = var.create_hosted_zone ? 1 : 0
#   name  = "nexus-net.net."
# }

# variable "create_hosted_zone" {
#   description = "Set to true to create the hosted zone, false otherwise."
#   type        = bool
#   default     = true
# }

# resource "aws_route53_record" "www" {
#   count = var.create_hosted_zone ? 1 : 0
#   zone_id = aws_route53_zone.nexusnet[0].zone_id
#   name    = "www.${aws_route53_zone.nexusnet[0].name}"  # Use the full domain name and end with a dot
#   type    = "A"

#   alias {
#     name                   = aws_cloudfront_distribution.nexusnet-distribution.domain_name
#     zone_id                = aws_cloudfront_distribution.nexusnet-distribution.hosted_zone_id
#     evaluate_target_health = false
#   }
#   depends_on = [aws_route53_zone.nexusnet]
# }

# resource "aws_acm_certificate" "cert-nexusnet" {
#   domain_name              = "nexus-net.net"
#   validation_method        = "DNS"
#   subject_alternative_names = ["www.nexus-net.net"]

#   lifecycle {
#     create_before_destroy = true
#   }
# }

# resource "aws_acm_certificate_validation" "cert-validation" {
#   certificate_arn         = aws_acm_certificate.cert-nexusnet.arn
#   validation_record_fqdns = [for record in aws_acm_certificate.cert-nexusnet.domain_validation_options : record.resource_record_name]
#   depends_on              = [aws_route53_zone.nexusnet]
# }

# resource "aws_cloudfront_cache_policy" "api_gateway_optimized" {
#   name        = "ApiGatewayOptimized"

#   default_ttl = 0
#   max_ttl     = 0
#   min_ttl     = 0

#   parameters_in_cache_key_and_forwarded_to_origin {
#     cookies_config {
#       cookie_behavior = "none"
#     }

#     headers_config {
#       header_behavior = "none"
#     }
#     query_strings_config {
#       query_string_behavior = "none"
#     }
#   }
# }

# resource "aws_cloudfront_origin_request_policy" "api_gateway_optimized" {
#   name    = "ApiGatewayOptimized"

#   cookies_config {
#     cookie_behavior = "none"
#   }

#   headers_config {
#     header_behavior = "whitelist"
#     headers {
#       items = ["Accept-Charset", "Accept", "User-Agent", "Referer"]
#     }
#   }

#   query_strings_config {
#     query_string_behavior = "all"
#   }
# }

# resource "aws_cloudfront_distribution" "nexusnet-distribution" {
  
#   depends_on = [ aws_s3_bucket.frontend, aws_s3_bucket.usermanager ]
  
#   enabled  = true
#   price_class = "PriceClass_All"

#   origin {
#     domain_name = "${aws_s3_bucket.frontend.id}.s3-website-${aws_s3_bucket.frontend.region}.amazonaws.com"
#     origin_id   = "s3-${aws_s3_bucket.frontend.id}"

#     custom_origin_config {
#       http_port                = 80
#       https_port               = 443
#       origin_keepalive_timeout = 5
#       origin_protocol_policy   = "http-only"
#       origin_read_timeout      = 30
#       origin_ssl_protocols     = ["TLSv1.2"]
#     }
#   }

#   default_cache_behavior {
#     allowed_methods        = ["GET", "HEAD", "OPTIONS"]
#     cached_methods         = ["GET", "HEAD", "OPTIONS"]
#     target_origin_id       = "s3-${aws_s3_bucket.frontend.id}"
#     viewer_protocol_policy = "redirect-to-https"
#     compress               = true
#     forwarded_values {
#       query_string = true
#       cookies {
#         forward = "all"
#       }
#       headers = ["Access-Control-Request-Headers", "Access-Control-Request-Method", "Origin"]
#     }
#   }


#   origin {
#     domain_name = replace(
#       replace(
#         aws_apigatewayv2_stage.default_api.invoke_url,
#         "https://",
#         ""
#       ),
#       "/api",
#       ""
#     )
#     origin_id = "api-gateway-default"
#     custom_origin_config {
#       http_port              = 80
#       https_port             = 443
#       origin_protocol_policy = "https-only"
#       origin_ssl_protocols   = ["TLSv1.2"]
#     }
#   }


#   ordered_cache_behavior {
#     allowed_methods = ["GET", "HEAD", "OPTIONS", "POST", "PUT", "DELETE", "PATCH"]
#     cached_methods  = ["GET", "HEAD", "OPTIONS"]
#     path_pattern = "api*"
#     target_origin_id = "api-gateway-default"
#     viewer_protocol_policy = "https-only"
#     cache_policy_id = aws_cloudfront_cache_policy.api_gateway_optimized.id
#     origin_request_policy_id = aws_cloudfront_origin_request_policy.api_gateway_optimized.id
#   }


#   restrictions {
#     geo_restriction {
#       # restriction_type = "none"
#       restriction_type = "whitelist"
#       locations        = ["CH"]
#     }
#   }

#   # Use custom SSL certificate
#   viewer_certificate {
#     acm_certificate_arn      = aws_acm_certificate.cert-nexusnet.arn
#     ssl_support_method       = "sni-only"
#     minimum_protocol_version = "TLSv1.2_2019"
#   }

#   # Set up custom domain names (CNAMEs)
#   aliases = ["nexus-net.net", "www.nexus-net.net"]

#   # Enable IPv6
#   is_ipv6_enabled = true
# }