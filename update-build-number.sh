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
    if [ "$TRAVIS_BRANCH" == "master" ]; then
        echo -e "Updating build number in production..."
        update_build_number "salume-prod"
        echo -e "Done updating build number in production."
    fi

    if [ "$TRAVIS_BRANCH" == "staging" ]; then
        echo -e "Updating build number in staging..."
        update_build_number "salume-staging"
        echo -e "Done updating build number in staging."
    fi
}
main

