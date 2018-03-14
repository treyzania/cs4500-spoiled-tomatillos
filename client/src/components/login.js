import React, { Component } from 'react';

class Login extends Component {
  handleSubmit(e) {
    var user = document.getElementById('lubox').value
    var pass = document.getElementById('lpbox').value
    var url = "/api/user/create"
    fetch(url, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        "username": user,
        "password": pass
      })
    })      
    document.login.action = "/"
  }

  handleRegister() {
    window.location.href="/register"
  }

  render() {  
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
