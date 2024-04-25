resource "aws_dynamodb_table" "user_info" {
  name              = "UserInfo"  # Set the name of the DynamoDB table for user info
  billing_mode      = "PAY_PER_REQUEST"  # Set the billing mode for the table
  hash_key          = "id"  # Set the hash key attribute for the table
  attribute {
    name = "id"
    type = "S"  # Set the data type of the attribute (String in this case)
  }
  attribute {
    name = "username"
    type = "S"
  }
  global_secondary_index {
    name               = "UsernameIndex"  # Set the name of the global secondary index
    hash_key           = "username"  # Set the hash key attribute for the index
    projection_type    = "ALL"  # Set the projection type for the index
  }
}

resource "aws_dynamodb_table" "follow" {
  name              = "Follow"  # Set the name of the DynamoDB table for follow
  billing_mode      = "PAY_PER_REQUEST"  # Set the billing mode for the table
  hash_key          = "userId"  # Set the hash key attribute for the table
  attribute {
    name = "userId"
    type = "S"  # Set the data type of the attribute (String in this case)
  }
}


################## SAMPLE ###########################
# resource "aws_dynamodb_table" "notes" {
#   name           = "notes"
#   billing_mode   = "PROVISIONED"
#   read_capacity  = 1
#   write_capacity = 1
#   hash_key       = "id"

#   attribute {
#     name = "id"
#     type = "S"
#   }
# }