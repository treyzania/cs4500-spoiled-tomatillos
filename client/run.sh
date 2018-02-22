#!/bin/bash

set -ex

# Set up environment dirs.
thisdir=$(basename $(realpath .))
projdir=$(realpath ..)
rundir=$(mktemp -d)

# Copy the files and go to them.
cp -r $projdir/* $rundir
ls -l $rundir
cd $rundir/$thisdir

exec npm install
exec npm install react-script@1.1.1 -g
exec npm -g install serve

exec npm start
