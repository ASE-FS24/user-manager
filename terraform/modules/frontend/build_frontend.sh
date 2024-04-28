#!/bin/bash

# Store current directory path
CURRENT_DIR=$(pwd)

# Retrieve API endpoint URLs
USER_API_ENDPOINT=$(aws apigatewayv2 get-apis --query "Items[?Name=='usermanager-api'].ApiEndpoint" --output text)
POST_API_ENDPOINT=$(aws apigatewayv2 get-apis --query "Items[?Name=='postmanager-api'].ApiEndpoint" --output text)

# Add a slash at the end of each API endpoint
USER_API_ENDPOINT="${USER_API_ENDPOINT}/"
POST_API_ENDPOINT="${POST_API_ENDPOINT}/"

# Output the updated API endpoints
echo "User Manager API Endpoint: ${USER_API_ENDPOINT}"
echo "Post Manager API Endpoint: ${POST_API_ENDPOINT}"

# Update .env file
sed -i "s|REACT_APP_USER_BASEURL=.*|REACT_APP_USER_BASEURL=${USER_API_ENDPOINT}|g" /root/nexus-net/frontbackallwithgits/frontend/.env
sed -i "s|REACT_APP_POST_BASEURL=.*|REACT_APP_POST_BASEURL=${POST_API_ENDPOINT}|g" /root/nexus-net/frontbackallwithgits/frontend/.env

# Change directory to the frontend folder
cd /root/nexus-net/frontbackallwithgits/frontend

# Run npm build
npm install
npm run build

# Copy build folder contents to the user-manager directory
cp -r /root/nexus-net/frontbackallwithgits/frontend/build /root/nexus-net/frontbackallwithgits/user-manager/

# Change back to the original directory
cd "$CURRENT_DIR"