import os

import copy
import json

import urllib
import requests

from flask import Flask, request, session, g, redirect, url_for, abort, render_template, flash

session_cookie_name = 'sessiontoken'

def convert_rest_url(endpoint):
	return 'http://' + os.getenv('REST_ADDR', '172.0.0.20:8080') + endpoint

def get_secret_key():
	with open('/mnt/rttmp/adminsecret.txt') as f:
		return f.read()

def get_session_cookie():
	if not session_cookie_name in request.cookies:
		print('no cookies passed!  cookie monster sad.')
		return None
	else:
		return request.cookies[session_cookie_name]

def get_current_user():
	sc = get_session_cookie()
	if sc is None:
		return None
	cookies = {session_cookie_name: sc}
	print('Looking up session of token ' + sc)
	req = requests.get(convert_rest_url('/api/user/current'), cookies=cookies)
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return None

def get_unread_notifications_as_rendered():
	sc = get_session_cookie()
	if sc is None:
		return None
	req = requests.get(convert_rest_url('/api/notifications/unread'), cookies={session_cookie_name: sc})
	if req.status_code == 200:
		rendered = []
		for r in json.loads(req.content):
			r2 = copy.copy(r)
			r2['contents'] = render_notification_msg(r['contents'])
			r2['raw'] = r['contents']
			rendered.append(r2)
		return rendered

bad_component_str = '<span class="notification_link_bad_argument">BAD_COMPONENT</span>'
def render_notification_msg(src):
	out = '&nbsp;'
	i = iter(src)
	state = 'start'
	tbuf = ''
	argbuf = ''
	try:
		while True:
			v = next(i)
			if state == 'start' and v == '{':
				state = 'b1'
				continue
			if state == 'b1':
				if v == '{':
					state = 'findtype'
				else:
					# Just go back to the start state and ignore the brace.
					out += '{'
					state = 'start'
				continue
			if state == 'findtype':
				if v == ':':
					state = 'findarg'
				else:
					tbuf += v
				continue
			if state == 'findarg':
				if v == '}':
					state = 'b2'
				else:
					argbuf += v
				continue
			if state == 'b2':
				if v == '}':
					out += __render_notification_component(tbuf, argbuf)
				else:
					out += '{{%s:%s}' % (tbuf, argbuf)
				state = 'start'
				tbuf = ''
				argbuf = ''
				continue
			out += v
	except:
		# This horrible branching is to figure out how much of the end of the
		# string we should reconstruct.
		if state == 'b1':
			return out + '{'
		elif state == 'findtype':
			return out + '{{' + tbuf
		elif state == 'findarg':
			return out + '{{%s:%s' % (tbuf, argbuf)
		elif state == 'b2':
			return out + '{{%s:%s}' % (tbuf, argbuf)
		else:
			return out
	return out + bad_component_str # Shouldn't be necessary.

def __render_notification_component(tbuf, argbuf):
	try:
		if tbuf == 'user':
			uid = int(argbuf)
			return '<a href="/user/%s">%s</a>' % (uid, get_user_by_id(uid)['username'])
		elif tbuf == 'title':
			tid = int(argbuf)
			return '<a href="/title/%s">%s</a>' % (tid, get_user_by_id(tid)['name'])
		else:
			return bad_component_str
	except:
		return bad_component_str

def get_user_by_id(id):
	req = requests.get(convert_rest_url("/api/user/%s" % id))
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return None

def get_title_by_id(id):
	req = requests.get(convert_rest_url("/api/title/%s" % id))
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return None

def get_reviews_by_title_id(id):
	req = requests.get(convert_rest_url("/api/title/%s/review/all" % str(id)))
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return None

def get_user_current_friends():
	cookies = {session_cookie_name: get_session_cookie()}
	req = requests.get(convert_rest_url('/api/friends/list'), cookies=cookies)
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return [{'username': 'error ' + req.status_code}]

def get_friend_requests():
	cookies = {session_cookie_name: get_session_cookie()}
	req = requests.get(convert_rest_url('/api/friends/request/recieved'), cookies=cookies)
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return None

def get_friend_requests_sent():
    cookies = {session_cookie_name: get_session_cookie()}
    req = requests.get(convert_rest_url('/api/friends/request/sent'), cookies=cookies)
    if req.status_code == 200:
        return json.loads(req.content)
    else:
        return None

def check_friends_status(other):
    cookies = {session_cookie_name: get_session_cookie()}
    req = requests.get(convert_rest_url('/api/friends/status?other=%s' % other), cookies=cookies)
    if req.status_code == 200:
        return req.content.decode('utf-8')
    else:
        return "error %s" % req.status_code

def submit_local_search(query):
	req = requests.get(convert_rest_url('/api/search?query=%s' % urllib.parse.quote(query)))
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return None

def find_title_by_source_params(src, srcid):
	req = requests.get(convert_rest_url('/api/title/by-full-source?source=%s,%s' % (src, srcid)))
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return None

create_title_endpoint = '/api/title/create'
def create_title(name, year, summary, source, sourceid, posterpath):
	body = {
		'name': name,
		'year': year,
		'desc': summary,
		'src': str(source) + ',' + str(sourceid),
		'img': posterpath,
	}
	req = requests.post(convert_rest_url(create_title_endpoint), headers={'Admin-Secret': get_secret_key()}, data=body)
	if req.status_code == 200:
		return json.loads(req.content)
	else:
		return None

tmdb_api_key = '53f856f34ff5b6efc67de7e14ac5617d'
tmdb_search_url = 'https://api.themoviedb.org/3/search/movie?api_key=%s&language=en-US&query=%s&include_adult=false'
def submit_tmdb_search(query):
	fullurl = tmdb_search_url % (tmdb_api_key, urllib.parse.quote(query))
	req = requests.get(fullurl)
	if req.status_code == 200:
		# This just returns the first page of results.
		return json.loads(req.content)['results']
	else:
		return None
