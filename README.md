# Salume [![Build Status](https://travis-ci.org/rafaelfiume/Salume.svg?branch=master)](https://travis-ci.org/rafaelfiume/Salume)

Check the application spec [here](http://rafaelfiume.github.io/Salume/). Access the [status page](http://app.rafaelfiume.com/salume/supplier/status/) in production.

## Running the application locally

The following environment variable must be set: $DATABASE_URL.

Build with:

    $mvn clean install

Then run it with:

    $java -Dprofile=dev $JAVA_OPTS -cp Supplier/target/classes:Supplier/target/dependency/* com.rafaelfiume.salume.SupplierApplication

The port can be specified using --PORT (e.g. --PORT=8081)

Use --debug to see DEBUG level messages.