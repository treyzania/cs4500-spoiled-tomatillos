#!/usr/bin/env python3

import os

import urllib3
import json

from flask import Flask, request, session, g, redirect, url_for, abort, render_template, flash

import rtapi

# Initialize
app = Flask(__name__)
app.config.from_object(__name__)

http = urllib3.PoolManager()

@app.route('/')
def main_page():
	return render_template('index.html')

@app.route('/title/<int:movie_id>')
def route_title(movie_id=None):

	# Make the database lookup
	movieobj = json.loads(http.request('GET', rtapi.convert_rest_url('/api/title/%s' % movie_id)).data)
	if movieobj is None:
		return 'id not found'

	# Render the template.
	return render_template('title.html', movie=movieobj)

app.run(host='0.0.0.0')
