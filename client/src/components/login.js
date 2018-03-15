import React, { Component } from 'react';

class Login extends Component {
  handleSubmit(e) {
    var user = document.getElementById('lubox').value
    var pass = document.getElementById('lpbox').value
    var sendBody = "username="+user+"&password="+pass
    var url = "/api/session/login"
    fetch(url, {
      method: 'POST',
      credentials: 'include',
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
        console.log("cookie: "+document.cookie);
      });
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
