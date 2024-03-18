# Food Recipes
This git repository showcases a simple food recipes application written using Spring.

## Detail Overview
The application is a simple food recipes application with a few use cases such as:
* Registering a user
* Logging in a user
* Adding a recipe (REST only)
* Viewing a recipe (REST only)
* Deleting a recipe (REST only)
* Updating a recipe (REST only)
* Sending an email to a user when account is register to confirm the account
* Sending an email to a user when user doesn't remember their password

I've used some of the dependencies to achieve this:
* spring-validation to validate the input data.
* spring-data-jpa to interact with the database.
* spring-web to build the REST API.
* spring-security to secure the application.
* spring-boot-starter-mail to send emails to support some of the use cases.

The database is a MySQL database running in a docker container.

## Running
Ensure you have the **MySQL** database container up and running:
```
# Run docker compose up command in detach mode
docker-compose up -d
```
When running this application also ensure that you set up the following environment variables:
```
# DB_PWD - The DB password 
# DB_URL - The URL to reach the DB 
# DB_USERNAME - The DB username
# RECIPE_SERVER_URL - The URL to reach the server
# SMTP_USERNAME - The SMTP username
# SMTP_PWD - The SMTP password
# SMTP_HOST - The SMTP host
# SMTP_PORT - The SMTP port
```

For the email functionality to work, you need to set up a Gmail account and enable the "Less secure app access" in the security settings. 
Then set the environment variables to the Gmail account details.

## About
This repo is for my own educational purposes and is ongoing project I'm building to just learn and play mainly with security features. Feel free to use if you find it useful.
