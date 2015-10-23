#!/bin/bash

echo -e "\n\nConfiguring DATABASE_RELEASE_URL environment variable..."

if [ "$TRAVIS_BRANCH" == "staging"  ]; then
    echo -e "Setting DATABASE_RELEASE_URL to point to staging db"
    export DATABASE_RELEASE_URL = "$DATABASE_STAGING_URL"
fi

if [ "$TRAVIS_BRANCH" == "master"  ]; then
    echo -e "Setting DATABASE_RELEASE_URL to point to production db"
    export DATABASE_RELEASE_URL = "$DATABASE_PRODUCTION_URL"
fi

echo -e "Done configuring DATABASE_RELEASE_URL.\n\n"

