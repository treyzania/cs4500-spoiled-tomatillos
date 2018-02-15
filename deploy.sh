#!/bin/bash

repopath=$(dirname $(realpath $0))
mountpath='/opt/rottentomatillos'

function start_container() {
	docker run \
		-n $2 \
		-v $repopath:$mountpath \
		$3 \
		java -jar "$mountpath/$1/target/*.jar"
}

# This is the REST API wrapping the database.
start_container 'exec' 'rt-exec'
