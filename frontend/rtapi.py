import urllib3
import requests
import json

from flask import Flask, request, session, g, redirect, url_for, abort, render_template, flash

session_cookie_name = 'sessiontoken'

def convert_rest_url(endpoint):
    return 'http://172.0.0.20:8080' + endpoint

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
