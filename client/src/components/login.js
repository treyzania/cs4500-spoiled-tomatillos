import React, { Component } from 'react';

class Login extends Component {
  handleSubmit(e) {
    var user = document.getElementById('ubox').value
    var pass = document.getElementById('pbox').value
    var url = `/api/user/create?username=${user}&password=${pass}`
    fetch(url).then((res) => res.json()).then((data) => {
      console.log(data);
    })
      
    document.register.action = "/"
  }

  render() {  
    return (
        <div className="row">
           <form    
            name="register"
            className="registerForm"
            onSubmit={(e) => this.handleSubmit(e)}
          >
            <input id="ubox" className="user" type="text" placeholder="Username"/>
            <br/>
            <input id="pbox" className="password" type="password" placeholder="Password"/>
            <br/>
            <button type="button" className="btn btn-primary btn-sm">Register</button>
          </form>
      </div>
    )
  }
}

export default Login;
