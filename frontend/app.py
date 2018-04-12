#!/usr/bin/env python3

import os
import sys
import traceback

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
	try:
		user = rtapi.get_current_user()
		nfs = rtapi.get_unread_notifications_as_rendered()
		return render_template('index.html', user=user, notifications=nfs)
	except:
		t, v, trace = sys.exc_info()
		return render_template('error.html', errtype=t, errval=v, errtrace=traceback.format_tb(trace))

@app.route('/login')
def route_login():
	try:
		u = rtapi.get_current_user()
		if u is not None:
			return redirect(url_for('main_page'))
		else:
			return render_template('login.html', user=u)
	except:
		t, v, trace = sys.exc_info()
		return render_template('error.html', errtype=t, errval=v, errtrace=traceback.format_tb(trace))

@app.route('/register')
def route_register():
	try:
		u = rtapi.get_current_user()
		if u is not None:
			return redirect(url_for('main_page'))
		else:
			return render_template('register.html')
	except:
		t, v, trace = sys.exc_info()
		return render_template('error.html', errtype=t, errval=v, errtrace=traceback.format_tb(trace))

@app.route('/me')
def route_me():
	try:
		u = rtapi.get_current_user()
		if u is None:
			return redirect(url_for('route_login'))
		else:
			return render_template('user.html', pageuser=u, user=u)
	except:
		t, v, trace = sys.exc_info()
		return render_template('error.html', errtype=t, errval=v, errtrace=traceback.format_tb(trace))

@app.route('/friends')
def route_friends():
	try:
		u = rtapi.get_current_user()
		if u is None:
			return redirect(url_for('route_login'))
		else:
			friends = rtapi.get_user_current_friends()
			reqs = rtapi.get_friend_requests()
			sent = rtapi.get_friend_requests_sent()
			return render_template('friends.html', user=u, friends=friends, reqs=reqs, reqssent=sent)
	except:
		t, v, trace = sys.exc_info()
		return render_template('error.html', errtype=t, errval=v, errtrace=traceback.format_tb(trace))

@app.route('/title/<int:movie_id>')
def route_title(movie_id=None):
	try:

		# Make the database lookup
		req = requests.get(rtapi.convert_rest_url('/api/title/%s' % movie_id))
		movieobj = json.loads(req.content)
		if movieobj is None:
			return 'title %s not found' % movie_id

		reviews = rtapi.get_reviews_by_title_id(movie_id)
		if reviews is None:
			return 'error loading reviews for ID %s' % movie_id

		friends = rtapi.get_user_current_friends()

		# Render the template.
		return render_template('title.html', movie=movieobj, reviews=reviews, friends=friends, user=rtapi.get_current_user())
	except:
		t, v, trace = sys.exc_info()
		return render_template('error.html', errtype=t, errval=v, errtrace=traceback.format_tb(trace))

@app.route('/user/<int:user_id>')
def route_user(user_id=None):
	try:
		# Find the user's ID
		req = requests.get(rtapi.convert_rest_url('/api/user/%s' % user_id))
		userobj = json.loads(req.content)
		if userobj is None:
			return 'user not found'

		cu = rtapi.get_current_user()
		fstat = rtapi.check_friends_status(userobj['username'])
		return render_template('user.html', pageuser=userobj, user=cu, fstatus=fstat)
	except:
		t, v, trace = sys.exc_info()
		return render_template('error.html', errtype=t, errval=v, errtrace=traceback.format_tb(trace))

@app.route('/search')
def route_search():
	try:
		search_query = request.args.get('query')
		if search_query is None:
			return 'you didn\'t provide a search query, asshole'
		local_titles = rtapi.submit_local_search(search_query)
		tmdb_titles = rtapi.submit_tmdb_search(search_query)
		results = list(local_titles) # Make a copy of what we have.
		for r in tmdb_titles:
			tmdb_id = int(r['id'])
			local_response = rtapi.find_title_by_source_params('tmdb', tmdb_id)
			if len(local_response) == 0:
				year = 0
				try:
					year = int(r['release_date'][:4])
				except:
					year = -1
				image = 'http://placehold.it/500x750'
				if 'poster_path' in r and r['poster_path'] is not None:
					image = 'https://image.tmdb.org/t/p/w500' + r['poster_path']
				res = rtapi.create_title(r['title'], year, r['overview'], 'tmdb', tmdb_id, image)
				if res is not None:
					results.append(res)
		return render_template('search_results.html', user=rtapi.get_current_user(), query=search_query, results=results)
	except:
		t, v, trace = sys.exc_info()
		return render_template('error.html', errtype=t, errval=v, errtrace=traceback.format_tb(trace))

app.run(host='0.0.0.0')
