import React, { Component } from 'react';

class Login extends Component {
  handleSubmit(e) {
    var term = document.getElementById('ubox').value
    var url = "/api/user/create"
    fetch(url, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username: term
      })
    })
    document.register.action = "/"
  }

  render() {  
    return (
      <div className="search-bar">
         <style dangerouslySetInnerHTML={{__html: `
              .search-bar { padding: 15px 10% }
              .searchbox { width: 50%; height: 25px }
              .searchbutton { height: 30px }
            `}} />
        <div className="row">
          <div className="col2">
   <form    
    name="register"
    className="registerForm"
    onSubmit={(e) => this.handleSubmit(e)}
  >
    <input id="ubox" className="user" type="text" placeholder="Username"/>
    <button className="btn">Register</button>
  </form>
          </div>
        </div>
      </div>
    )
  }
}

export default Login;
