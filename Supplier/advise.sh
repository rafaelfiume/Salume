#!/usr/bin/env bash

base_message="\nRequesting product advise for magic profile"

server_option="${1}"
case $server_option in
    "prod") SUPPLIER_BASE_URL="http://app.rafaelfiume.com/salume/supplier"
        echo -e "$base_message in production"
    ;;
    "stag") SUPPLIER_BASE_URL="$SUPPLIER_STAGING_URL"
        echo -e "$base_message in staging"
    ;;
    "dev" ) SUPPLIER_BASE_URL="http://localhost:8081/salume/supplier"
        echo -e "$base_message in staging"
    ;;
esac

echo `curl \
     -X GET \
     -H "Accept: application/xml" \
     "$SUPPLIER_BASE_URL"/advise/for/Healthy` | xmllint --format -
