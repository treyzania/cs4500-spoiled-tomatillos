#!/bin/bash

repopath=$(dirname $(realpath $0))
mountpath='/opt/rottentomatillos'

function start_container() {
	cd $repopath
	jarpath=$(ls $1/target/*.jar)
	docker run \
		--name=$2 \
		-v $repopath:$mountpath \
		$3 \
		openjdk \
		java -jar "$mountpath/$jarpath"
}

# Prune container if applicable.
if [ "$1" != "--no-prune" ]; then
	docker container prune -f
fi

# This is the REST API wrapping the database.
start_container 'exec' 'rt-exec'
