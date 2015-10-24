#!/usr/bin/env bash

echo -e "Requesting product advise for magic profile"

 echo `curl \
     -X GET \
     -H "Accept: application/xml" \
     http://localhost:8081/salume/supplier/advise/for/Healthy` | xmllint --format -
