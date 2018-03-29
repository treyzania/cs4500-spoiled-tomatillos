import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';

class Register extends Component {
  constructor(props) {
    super(props)
    this.state = {
      wordTooShort: false
    }
  }

  handleSubmit(e) {
    var user = document.getElementById('rubox').value
    var pass = document.getElementById('rpbox').value    
    var sendBody = "username="+user+"&password="+pass
    var url = "/api/user/create"
    if (user.length >= 3 && pass.length >= 8) {
      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: sendBody
      }) 
      this.props.history.push('/');
    } else {
      this.setState({ wordTooShort: true });
      e.preventDefault();
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
          <Auth wordTooShort={this.state.wordTooShort}/>
        </form>
    )
  }
}

function Auth(params) {
  console.log(params.wordTooShort);
  if (params.wordTooShort) {
    console.log("Warning");
    return (
      <div>
      <div class="alert alert-danger">
        Password must be greater than 8.
      </div>
      <div className="form-group text-center">           
        <button type="button" className="btn btn-outline-danger btn-sm">Register</button>
      </div>
      </div>
    );
 }
 console.log("nothing unusual");
 return (
      <div className="form-group text-center">           
        <button type="submit" className="btn btn-primary btn-sm">Register</button>
      </div>
  );
}

export default withRouter(Register);
