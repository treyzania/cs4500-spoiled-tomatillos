#!/usr/bin/env python3

from flask import Flask, render_template
import docker

dclient = docker.from_env()

# Initialize
app = Flask(__name__)
app.config.from_object(__name__)

@app.route('/status')
def status_page():
	return render_template('index.html', containers=get_containers())

def get_containers():
	containers = dclient.containers.list()
	out = []
	for c in containers:
		out.append({
			'id': c.id[:7],
			'image': c.image,
			'name': c.name,
			'status': c.status
		})
	return out

app.run(host='0.0.0.0')
