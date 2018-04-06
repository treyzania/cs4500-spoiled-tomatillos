#!/bin/bash

set -ex

# Install packages.
pip3 install --user flask urllib3

# Set up our environment.
mkdir -p /app
cp -r . /app
cd /app

# Run it.
exec python3 app.py runserver
