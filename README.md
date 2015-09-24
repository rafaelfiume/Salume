# Salume

A Jetty embedded web application

## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $java $JAVA_OPTS -cp Supplier/target/classes:Supplier/target/dependency/* com.rafaelfiume.salume.SupplierApplication

You can also use the --debug flag to see DEBUG level messages.