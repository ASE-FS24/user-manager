#!/bin/sh

# Define the S3 bucket name
bucket_name="nexus-net-user-info-bucket"

# Create the S3 bucket
aws s3 mb s3://$bucket_name

aws s3 cp ./target/original-usermanager.jar s3://$bucket_name/usermanager.jar

# List the contents of the S3 bucket
awslocal --endpoint-url=http://localhost:4566 s3 ls s3://$bucket_name

# URL of the website for S3 bucket
website_url="https://app.localstack.cloud/inst/default/resources/s3"

# Message to display
message="Check your S3 bucket resources here:"

# Print the message and URL to the console
echo "$message $website_url"