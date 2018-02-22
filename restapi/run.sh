#!/bin/bash

set -ex

# Set up environment dirs.
thisdir=$(basename $(realpath .))
projdir=$(realpath ..)
rundir=$(mktemp -d)

# Copy the files and go to them.
cp -r $projdir/* $rundir
ls -l $rundir
cd $rundir

# Make sure all the dependencies are available.
mvn clean install

# Now actually run it.
cd $thisdir
exec mvn spring-boot:run
