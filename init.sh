#!/bin/bash

# Create a Lambda function for a Spring Boot application
aws --endpoint-url=http://localhost:4566 lambda create-function \
    --function-name user-manager-lambda \
    --runtime java17 \
    --handler ch.nexusnet.usermanager.aws.LambdaHandler \
    --zip-file fileb://./target/usermanager-0.0.1-SNAPSHOT.jar \
    --role arn:aws:iam::000000000000:role/DummyRole
