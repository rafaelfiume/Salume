#!/bin/bash

function update_build_number() {
    application=$1
    curl -n -s \
        -X PATCH \
        -H "Content-Type: application/json" \
        -H "Accept: application/vnd.heroku+json; version=3" \
        -H "Authorization: Bearer $HEROKU_API_TOKEN" \
        -d "{ \"BUILD_NUMBER\" : $TRAVIS_BUILD_NUMBER }" \
        https://api.heroku.com/apps/$application/config-vars > /dev/null
}

main() {
    echo -e "Updating build number in production...\n"
    update_build_number "salume-prod"
    echo -e "Build number updated. \n"

    echo -e "Updating build number in staging...\n"
    update_build_number "salume-staging"
    echo -e "Done updating build number. \n"
}
main

