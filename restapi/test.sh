#!/bin/bash

set -ex

# Create user and login
curl -X POST --data 'username=foo&password=bar' localhost/api/user/create
curl -X POST --data 'username=foo&password=bar' localhost/api/session/login

# Create a title for simple usage.
curl -X POST --data 'name=somemovie&year=2000&desc=this was a great movie' localhost/api/title/create

