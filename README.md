# Salume [![Build Status](https://travis-ci.org/rafaelfiume/Salume.svg?branch=master)](https://travis-ci.org/rafaelfiume/Salume) [![Apache 2.0 License](https://img.shields.io/badge/license-Apache_2.0-blue.svg)](https://github.com/rafaelfiume/Salume/blob/master/LICENSE)

Check the application spec [here](http://rafaelfiume.github.io/Salume). Access the [status page](http://app.rafaelfiume.com/salume/supplier/status) in production.

This application is continuous delivered every time code is pushed into master and the build passes. 

## Running the Application

#### Using Maven

Build with:

    $mvn clean install

Optionally recreate the database running the command:

    $mvn clean install -PdbRecreate

Then starts Supplier with:

    $java -Dprofile=dev $JAVA_OPTS -cp Supplier/target/classes:Supplier/target/dependency/* com.rafaelfiume.salume.SupplierApplication

Use --debug to see DEBUG level messages.

#### Using Intellij IDEA

Because this project uses the [Lombok](https://projectlombok.org) library, follow these steps:

* Select "Enable annotation processing" option at Preferences... > Build, Execution, Deployment > Compiler > Annotation Processors
* Install the [lombok-intellij-plugin](https://github.com/mplushnikov/lombok-intellij-plugin)

## Environment Variables

The following variables are used in this project:

#### Travis Only
* $DATABASE_RELEASE_URL is optional, but recommended. Specify a value to it to run db scripts in the release environment (staging, prod).

#### Travis / Heroku
* $DATABASE_URL is mandatory and points to the database. It must be set since there's no default value.
* $PROFILE is mandatory (valid values are: 'dev', 'staging', 'prod').
* $PORT is mandatory for 'staging' and 'prod' profiles, but set by Heroku. When necessary $PORT can be specified using --PORT=8081, for instance.

#### Maven
* $GITHUB_OAUTH_TOKEN (optional) is necessary when pushing Supplier-Acceptance-Tests into mvn-repo


## TODO List - aka User Stories ;)

Consider to use Kotlin instead of Java.

### Best Offering According to Customer Profile Rest Service (Salami Product Only)
* ~~Suggest up to three different products according to client profile~~
* ~~Only suggest traditional products to experts~~
* ~~Better product advice using Wikipedia images and descriptions~~

## Non-Functional Stories

### Project Configuration
* ~~Walking Skeleton: a Draft of a Status Page~~
* ~~Configure Travis CI build and automatic spec publication.~~
* ~~Deploy app into Heroku~~

### Provide Status Page
* ~~Show status Ok when app is up and running and all the resources it depends on are available~~
* ~~Show status Fail when app is up and running and database connection fails~~

### Database Recreator Plugin
* ~~Recrete Db when specifying dbRecreator profile in the maven build~~
* ~~Possibility of using more than one script file to recreate the db~~
* ~~Load db with more products~~
