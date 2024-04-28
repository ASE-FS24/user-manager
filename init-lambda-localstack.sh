#!/bin/bash

# Create a Lambda function for a Spring Boot application
aws lambda create-function --function-name usermanager-lambda \
  --handler ch.nexusnet.usermanager.aws.LambdaHandler::handleRequest \
  --runtime java17 \
  --role arn:aws:iam::123456789012:role/execution_role \
  --package-type Zip \
  --code ImageUri=usermanager

# URL of the website
website_url="https://app.localstack.cloud/inst/default/resources/lambda/functions"

# Message to display
message="Check your Lambda Functions here:"

# Print the message and URL to the console
echo "$message $website_url"

aws lambda create-function --function-name lambda-direct \
--zip-file fileb://./target/original-usermanager.jar --handler ch.nexusnet.usermanager.aws.LambdaHandler::handleRequest --runtime java17 \
--role arn:aws:iam::730335642926:role/lambda-ex