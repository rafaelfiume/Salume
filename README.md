# Salume [![Build Status](https://travis-ci.org/rafaelfiume/Salume.svg?branch=master)](https://travis-ci.org/rafaelfiume/Salume) [![Apache 2.0 License](https://img.shields.io/badge/license-Apache_2.0-blue.svg)](https://github.com/rafaelfiume/Prosciutto-Mob/blob/master/LICENSE)


Check the application spec [here](http://rafaelfiume.github.io/Salume/). Access the [status page](http://app.rafaelfiume.com/salume/supplier/status/) in production.

## Running the Application

The following environment variable must be set:
* $DATABASE_URL
* $GITHUB_OAUTH_TOKEN

Build with:

    $mvn clean install

Then run it with:

    $java -Dprofile=dev $JAVA_OPTS -cp Supplier/target/classes:Supplier/target/dependency/* com.rafaelfiume.salume.SupplierApplication

Use --debug to see DEBUG level messages.

## Environment Variables

The following variables are used in this project:

#### Application
1) $DATABASE_URL is mandatory and points to the database. It must be set since there's no default value.
2) $DATABASE_RELEASE_URL is optional, but recommended.
Specify a value to it to run db scripts in the release environment (staging, prod).
3) $PORT is mandatory, but usually set by Heroku. When necessary $PORT can be specified using --PORT=8081, for instance.

## TODO List - aka User Stories ;)

### Best Offering According to Customer Profile Rest Service (Salume Product Only)
* ~~Suggest up to three different products according to client profile~~
* ~~Only suggest traditional products to experts~~


## TODO List 2 - aka Non-Functional Requirement Stories ;)

### Provide Status Page
* ~~Show status Ok when app is up and running and all the resources it depends on are available~~
* ~~Show status Fail when app is up and running and database connection fails~~

### Database Recreator Plugin
* ~~Recrete Db when specifying dbRecreator profile in the maven build~~
* ~~Possibility of using more than one script file to recreate the db~~
* Load db with more data
