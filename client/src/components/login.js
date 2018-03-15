import React, { Component } from 'react';
import Cookies from 'js-cookie';

class Login extends Component {
  handleSubmit(e) {
    var user = document.getElementById('lubox').value
    var pass = document.getElementById('lpbox').value
    var sendBody = "username="+user+"&password="+pass
    var url = "/api/session/login"
    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: sendBody
    })
      .then(response => {
        return response.json();
      })
      .then(responseJ => {
        console.log(responseJ);        
        Cookies.set('user', responseJ.user)
        Cookies.set('sessiontoken', responseJ.token)
        console.log("cookie-user: "+Cookies.get('user'));
      })
      .catch(error => console.error(error));
    document.login.action = "/"
  }

  handleRegister() {
    window.location.href="/register"
  }

  handleLogout() {
    var url = "/api/session/logout"
    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: "logout"
    })
      .then(response => {
        return response.json();
      })
      .then(responseJ => {
        console.log(responseJ);        
        Cookies.remove('user')
        Cookies.remove('sessiontoken')
        Cookies.remove('rating')
        this.setState({rating: 0});        
      })
      .catch(error => console.error(error));
    window.location.href = "/"   
  }

  render() {  
    if (Cookies.get('user') !== "" && Cookies.get('user') !== undefined || true) {
      return (
        <div>
          <h5>Welcome, {Cookies.get('user')}</h5>
          <button className="btn btn-danger" type="button" onClick={this.handleLogout.bind(this)}>Logout</button>
        </div>
      );
    }
    return (
         <form    
          name="login"
          className="loginForm"
          onSubmit={(e) => this.handleSubmit(e)}
        >
          <div className="form-group text-center">
            <input id="lubox" className="user" type="text" placeholder="Username"/>
          </div>
          <div className="form-group text-center">           
            <input id="lpbox" className="password" type="password" placeholder="Password"/>
          </div>
          <div className="form-group text-center">           
            <button id="llogin" type="submit" className="btn btn-primary btn-sm">Login</button>            
            <button id="lregister" type="button" className="btn btn-sm" onClick={() => this.handleRegister()}>Register</button>            
          </div>
        </form>
    )
  }
}

export default Login;
