import urllib
import requests
import json

from flask import Flask, request, session, g, redirect, url_for, abort, render_template, flash

session_cookie_name = 'sessiontoken'

def convert_rest_url(endpoint):
    return 'http://172.0.0.20:8080' + endpoint

def get_secret_key():
    with open('/mnt/rttmp/adminsecret.txt') as f:
        return f.read()

def get_current_user():
    # If the user didn't pass a session token then fail immediately
    if not session_cookie_name in request.cookies:
        print('no cookies passed!  cookie monster sad.')
        return None

    # Make the request, passing the session token along.
    cookies = {session_cookie_name: request.cookies[session_cookie_name]}
    print('Looking up session of token ' + cookies[session_cookie_name])
    req = requests.get(convert_rest_url('/api/user/current'), cookies=cookies)
    if req.status_code == 200:
        return json.loads(req.content)
    else:
        return None

def get_reviews_by_id(id):
    req = requests.get(convert_rest_url("/api/title/%s/review/all" % str(id)))
    if req.status_code == 200:
        return json.loads(req.content)
    else:
        return None

def submit_local_search(query):
    req = requests.get(convert_rest_url('/api/search?query=%s' % urllib.parse.quote(query)))
    if req.status_code == 200:
        return json.loads(req.content)
    else:
        return None

def find_title_by_source_params(src, srcid):
    url = convert_rest_url('/api/title/by-full-source?source=%s,%s' % (src, srcid))
    req = requests.get(url)
    if req.status_code == 200:
        return json.loads(req.content)
    else:
        return None

create_title_endpoint = '/api/title/create'
def create_title(name, year, summary, source, sourceid):
    body = {
        'name': name,
        'year': year,
        'desc': summary,
        'src': str(source) + ',' + str(sourceid)
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
