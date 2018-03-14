import React, { Component } from 'react';

class Register extends Component {
  handleSubmit(e) {
    var user = document.getElementById('rubox').value
    var pass = document.getElementById('rpbox').value
    var url = "/api/user/create"
    if (user != "" && pass != "") {
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
      document.register.action= "/"
    }
  }

  render() {  
    return (
         <form    
          name="register"
          className="registerForm"
          onSubmit={(e) => this.handleSubmit(e)}
        >
          <div className="form-group text-center">
            <input id="rnbox" className="name" type="text" placeholder="Full Name"/>
          </div> 
          <div className="form-group text-center">
            <input id="rubox" className="user" type="text" placeholder="Username"/>
          </div>
          <div className="form-group text-center">           
            <input id="rpbox" className="password" type="password" placeholder="Password"/>
          </div>
          <div className="form-group text-center">           
            <button type="submit" className="btn btn-primary btn-sm">Register</button>
          </div>
        </form>
    )
  }
}

export default Register;
