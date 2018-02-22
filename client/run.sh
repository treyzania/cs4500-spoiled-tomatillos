#!/bin/bash

set -ex

npm run build

exec serve -s build
