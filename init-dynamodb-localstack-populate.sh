#!/bin/sh

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user3"},
    "username": {"S": "john_doe88"},
    "firstName": {"S": "John"},
    "lastName": {"S": "Doe"},
    "birthday": {"S": "1998-05-20"},
    "bio": {"S": "Enthusiast of digital innovations and community service."},
    "university": {"S": "Tech University"},
    "degreeProgram": {"S": "Computer Science"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user4"},
    "username": {"S": "linda_physics"},
    "firstName": {"S": "Linda"},
    "lastName": {"S": "Berry"},
    "birthday": {"S": "2000-12-01"},
    "bio": {"S": "Physics major with a knack for quantum mechanics."},
    "university": {"S": "Science College"},
    "degreeProgram": {"S": "Physics"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user5"},
    "username": {"S": "dave_green"},
    "firstName": {"S": "Dave"},
    "lastName": {"S": "Green"},
    "birthday": {"S": "1997-07-30"},
    "bio": {"S": "Passionate about environmental changes and sustainability."},
    "university": {"S": "Green Earth University"},
    "degreeProgram": {"S": "Environmental Science"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user6"},
    "username": {"S": "emilyc94"},
    "firstName": {"S": "Emily"},
    "lastName": {"S": "Clark"},
    "birthday": {"S": "1994-09-16"},
    "bio": {"S": "Exploring the intersection of technology and education."},
    "university": {"S": "Metro College"},
    "degreeProgram": {"S": "Educational Technology"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user7"},
    "username": {"S": "markj"},
    "firstName": {"S": "Mark"},
    "lastName": {"S": "Johnson"},
    "birthday": {"S": "1996-11-08"},
    "bio": {"S": "Volunteer and community organizer."},
    "university": {"S": "State University"},
    "degreeProgram": {"S": "Sociology"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user8"},
    "username": {"S": "paula_s"},
    "firstName": {"S": "Paula"},
    "lastName": {"S": "Smith"},
    "birthday": {"S": "1998-03-14"},
    "bio": {"S": "Physics and math tutor, always ready to help."},
    "university": {"S": "Science College"},
    "degreeProgram": {"S": "Physics"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user9"},
    "username": {"S": "alice_w"},
    "firstName": {"S": "Alice"},
    "lastName": {"S": "Walker"},
    "birthday": {"S": "1995-02-23"},
    "bio": {"S": "Eco-warrior and wildlife enthusiast."},
    "university": {"S": "Green Earth University"},
    "degreeProgram": {"S": "Biology"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user10"},
    "username": {"S": "robert_z"},
    "firstName": {"S": "Robert"},
    "lastName": {"S": "Zimmerman"},
    "birthday": {"S": "1992-07-10"},
    "bio": {"S": "Tech enthusiast and coder."},
    "university": {"S": "Tech University"},
    "degreeProgram": {"S": "Computer Science"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user11"},
    "username": {"S": "nancy_p"},
    "firstName": {"S": "Nancy"},
    "lastName": {"S": "Peters"},
    "birthday": {"S": "2001-04-18"},
    "bio": {"S": "Aspiring physicist, ready to unlock the secrets of the universe."},
    "university": {"S": "Science College"},
    "degreeProgram": {"S": "Physics"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user12"},
    "username": {"S": "tommy_v"},
    "firstName": {"S": "Tommy"},
    "lastName": {"S": "Vercetti"},
    "birthday": {"S": "1996-10-05"},
    "bio": {"S": "Engaged in social work and public welfare."},
    "university": {"S": "Metro College"},
    "degreeProgram": {"S": "Public Administration"}
}'

awslocal --endpoint-url=http://localstack:4566 dynamodb put-item --table-name UserInfo --item '{
    "id": {"S": "user13"},
    "username": {"S": "clara_m"},
    "firstName": {"S": "Clara"},
    "lastName": {"S": "Morris"},
    "birthday": {"S": "1999-01-30"},
    "bio": {"S": "Environmental activist and part-time blogger."},
    "university": {"S": "Green Earth University"},
    "degreeProgram": {"S": "Environmental Science"}
}'