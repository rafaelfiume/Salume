# Salume [![Build Status](https://travis-ci.org/rafaelfiume/Salume.svg?branch=master)](https://travis-ci.org/rafaelfiume/Salume)

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
