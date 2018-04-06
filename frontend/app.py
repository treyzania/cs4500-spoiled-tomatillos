#!/usr/bin/env python3

import os

from flask import Flask, request, session, g, redirect, url_for, abort, render_template, flash

# Initialize
app = Flask(__name__)
app.config.from_object(__name__)

@app.route('/')
def main_page():
	return render_template('index.html')

app.run(host='0.0.0.0')
