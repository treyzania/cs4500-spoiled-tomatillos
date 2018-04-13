#!/bin/bash

set -ex

# Install packages.
pip3 install --user flask docker

# Set up our environment.
mkdir -p /app
cp -r . /app
cd /app

# Run it, with logs.
logfile=/mnt/rtpersist/logging/statusmon.log
mkdir -p $(dirname $logfile)
touch $logfile
python3 app.py runserver | tee -a $logfile
