import React, { Component } from 'react';
import Cookies from 'js-cookie';
import { withRouter } from 'react-router-dom';

class Login extends Component {
  constructor(props) {
    super(props)
    this.state = {
      failedLogin: false
    }
  }

  async handleSubmit(e) {
    var user = document.getElementById('lubox').value
    var pass = document.getElementById('lpbox').value
    var sendBody = "username="+user+"&password="+pass
    var url = "/api/session/login"
    await fetch(url, {
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
        Cookies.set('user', responseJ.user.username)
        Cookies.set('id', responseJ.user.id)
        Cookies.set('sessiontoken', responseJ.token)        
        console.log("cookie-user: "+Cookies.get('token'));
      })
      .catch(error => console.error(error));
    console.log("cookie "+Cookies.get('user'))
    if (Cookies.get('user') === undefined) {
      this.setState({ failedLogin: true });
      e.preventDefault();
    } else {
      this.props.history.push('/');
    }
  }

  handleRegister() {
    this.props.history.push('/register/');
  }

  handleLogout() {
    var url = "/api/session/logout"
    fetch(url, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: "logout"
    })
      .then(response => {
        return response.json();
      })
      .then(responseJ => {
        console.log("Logged out "+responseJ);        
      })
      .catch(error => console.error(error));
    Cookies.remove('user')
    Cookies.remove('sessiontoken')
    Cookies.remove('rating')
    Cookies.remove('id')
    this.setState({rating: 0});        
    this.props.history.push('/');
  }

  render() {  
    if (Cookies.get('user') !== "" && Cookies.get('user') !== undefined) {
      return (
        <div>
          <h5>Welcome, {Cookies.get('user')}</h5>
          <button className="btn btn-danger" type="button" onClick={this.handleLogout.bind(this)}>Logout</button>
        </div>
      );
    }
    return (
      <div>
        <Auth failedLogin={this.state.failedLogin}/>
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
      </div>
    )
  }
}

function Auth(params) {
  if (params.failedLogin) {
    console.log("wrong password");
    return (
      <div class="alert alert-danger">
        <strong>Danger!</strong> Wrong username or password.
      </div>
    );
 }
 console.log("nothing unusual");
 return (
    <div/>
  );
}

export default withRouter(Login);
