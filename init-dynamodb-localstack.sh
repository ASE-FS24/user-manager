#!/bin/sh

awslocal --endpoint-url=http://localhost:4566/ dynamodb create-table \
    --table-name UserInfo \
    --key-schema AttributeName=id,KeyType=HASH \
    --attribute-definitions AttributeName=id,AttributeType=S AttributeName=username,AttributeType=S \
    --billing-mode PAY_PER_REQUEST \
    --global-secondary-indexes '[
        {
            "IndexName": "UsernameIndex",
            "KeySchema": [{"AttributeName": "username", "KeyType": "HASH"}],
            "Projection": {"ProjectionType": "ALL"}
        }
    ]'


awslocal --endpoint-url=http://localhost:4566/ dynamodb create-table \
    --table-name Follow \
    --key-schema AttributeName=userId,KeyType=HASH \
    --attribute-definitions AttributeName=userId,AttributeType=S \
    --billing-mode PAY_PER_REQUEST
