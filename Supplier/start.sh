#!/bin/bash

echo "Starting Salume Supplier..."
java -Dprofile=dev $JAVA_OPTS -cp target/classes:target/dependency/* com.rafaelfiume.salume.SupplierApplication

echo "Salume Supplier started"
