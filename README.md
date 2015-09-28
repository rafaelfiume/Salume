# Salume

Check the application spec [here](http://rafaelfiume.github.io/Salume/).

You can access the [status page](http://frozen-mountain-3180.herokuapp.com/salume/supplier/status/) in production.

## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $java -Dprofile=dev $JAVA_OPTS -cp Supplier/target/classes:Supplier/target/dependency/* com.rafaelfiume.salume.SupplierApplication

You can also use the --debug flag to see DEBUG level messages.