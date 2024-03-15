# user-manager
This app handles User Authentication and Profile Management

# Usage
- Clone the repository
- compile the code using `mvn clean install`
- start Docker
- Run the command `docker-compose up` to start the application and localstack
- Run `source init-dynamodb-localstack.sh` to create the tables in localstack
- Run `source init-lambda-localstack.sh` to create the lambda functions in localstack