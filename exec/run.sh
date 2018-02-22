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

# Now actually run it.
exec mvn spring-boot:run
