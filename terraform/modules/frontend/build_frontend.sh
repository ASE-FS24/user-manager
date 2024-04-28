#!/bin/bash

# Store current directory path
CURRENT_DIR=$(pwd)

# Retrieve API endpoint URLs
USER_API_ENDPOINT=$(aws apigatewayv2 get-apis --query 'Items[0].ApiEndpoint' --output text)
POST_API_ENDPOINT=$(aws apigatewayv2 get-apis --query 'Items[1].ApiEndpoint' --output text)

# Update .env file
sed -i "s|REACT_APP_USER_BASEURL=.*|REACT_APP_USER_BASEURL=${USER_API_ENDPOINT}|g" /root/nexus-net/frontbackallwithgits/frontend/.env
sed -i "s|REACT_APP_POST_BASEURL=.*|REACT_APP_POST_BASEURL=${POST_API_ENDPOINT}|g" /root/nexus-net/frontbackallwithgits/frontend/.env

# Change directory to the frontend folder
cd /root/nexus-net/frontbackallwithgits/frontend

# Run npm build
npm run build

# Copy build folder contents to the user-manager directory
cp -r /root/nexus-net/frontbackallwithgits/frontend/build /root/nexus-net/frontbackallwithgits/user-manager/

# Change back to the original directory
cd "$CURRENT_DIR"