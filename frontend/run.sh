#!/bin/bash

set -ex

# Install packages.
pip3 install --user flask urllib3 requests

# Set up our environment.
mkdir -p /app
cp -r . /app
cd /app

# Run it, with logs.
python3 app.py runserver | tee -a /mnt/rtpersist/logging/flask.log
