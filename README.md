# user-manager
This app handles User Authentication and Profile Management

# Starting the app in dev mode

1. Start up docker-compose-localstack.yml
2. Publish the ports using:
   docker run -p 4566:4566 localstack/localstack
3. Run init-dynamodb-localstack.sh
4. Start the application by running UserManagerApplication with the dev profile

# Populating the database

1. Start up docker-compose-localstack.yml
2. Publish the ports using:
   docker run -p 4566:4566 localstack/localstack
3. Run init-dynamodb-localstack-populate.sh to create the table and populate it with posts, comments, and likes.

# Running Integration Tests

0. Disable VPN connection if you have one
1. Start Docker locally
2. Start integration Tests in the test folder

# Documentation

You can find the `index.html` in the `docs` directory. Open it in a browser to view the documentation.
You can update the documentation as needed by editing the `users.yaml` file in the `src/main/resources` directory and recompiling the project.
