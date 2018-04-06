#!/usr/bin/env python3

import os

import urllib3
import json
import requests

from flask import Flask, request, session, g, redirect, url_for, abort, render_template, flash

import rtapi

# Initialize
app = Flask(__name__)
app.config.from_object(__name__)

@app.route('/')
def main_page():
	return render_template('index.html', user=rtapi.get_current_user())

@app.route('/login')
def route_login():
	u = rtapi.get_current_user()
	if u is not None:
		return redirect(url_for('main_page'))
	else:
		return render_template('login.html', user=u)

@app.route('/register')
def route_register():
	u = rtapi.get_current_user()
	if u is not None:
		return redirect(url_for('main_page'))
	else:
		return render_template('register.html')

@app.route('/me')
def route_me():
	u = rtapi.get_current_user()
	if u is None:
		return redirect(url_for('route_login'))
	else:
		return render_template('user.html', userobj=u, user=u)

@app.route('/title/<int:movie_id>')
def route_title(movie_id=None):

	# Make the database lookup
	req = requests.get(rtapi.convert_rest_url('/api/title/%s' % movie_id))
	movieobj = json.loads(req.content)
	if movieobj is None:
		return 'title not found'

	# Render the template.
	return render_template('title.html', movie=movieobj, user=rtapi.get_current_user())

@app.route('/user/<int:user_id>')
def route_user(user_id=None):

	# Find the user's ID
	req = requests.get(rtapi.convert_rest_url('/api/title/%s' % user_id))
	userobj = json.loads(req.content)
	if userobj is None:
		return 'user not found'

	return render_template('user.html', user=userobj, u=rtapi.get_current_user())

app.run(host='0.0.0.0')
