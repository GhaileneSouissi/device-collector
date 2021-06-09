#!/bin/bash
FILE_VERSION=version.sbt
APP_NAME=device-collector-api


function extract_version() {
  echo `grep -oP '"[^"]+"' $FILE_VERSION | tr -d '"'`
}

raw_version=$(extract_version)
set_version=$raw_version

function test_step() {
  sbt test
}

function build_step() {
  sbt clean dist

  ls -l api/target/universal/
  unzip api/target/universal/$APP_NAME-${set_version}.zip -d api/target/universal/
  mv api/target/universal/$APP_NAME-$set_version $APP_NAME
}

function docker_build_step() {
  docker build -t $APP_NAME:$set_version \
   --build-arg application_name=$APP_NAME \
   --no-cache --pull .
}


function docker_run_step() {
  docker-compose up -d
}

if [[ $@ ]]; then
  docker_build_step
  docker_run_step
else
    test_step
    build_step
    docker_build_step
    docker_run_step
fi

