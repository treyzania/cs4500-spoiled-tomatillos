import React, { Component } from 'react';
import Cookies from 'js-cookie';
import { withRouter } from 'react-router-dom';

class User extends Component {
  constructor(props) {
    super(props)
    let match = this.props.match
    console.log("MATCH "+match);
    let id= -1
    if (match !== undefined && match.url !== "/") {
      let search = match.url;
      console.log("TERM "+search);
      id = search.substring(6, search.length);
      console.log("ID "+id);
    }
    this.state = {
      userID: id, // 106646
      userName: "",
      fl: [],
      rec: [],
    }
  }

  componentDidMount() {
    let url = "/api/user/"+this.state.userID;
    fetch(url)
      .then((resp) => resp.json())
      .then((data) => {console.log("CUR US "+data); this.setState({userName: data.username })})
      .catch((error) => console.log(error));
//logged in
    if (Cookies.get('user') !== undefined) {
     fetch("/api/friends/list", {
      method: 'GET',
      credentials: 'include'})
        .then((resp) => resp.json())
        .then((data) => {console.log("FList "+data); this.setState({ fL: data});})
        .catch((error) => console.error(error));
    fetch("/api/friends/request/recieved", {
      method: 'GET',
      credentials: 'include'})
        .then((resp) => resp.json())
        .then((data) => {console.log("RECEIVED "+data); this.setState({ rec: data});})
        .catch((error) => console.error(error));
    }
  }
  
  onStarClick(nextValue, prevValue, name) {
    console.log(nextValue);
    var sendBody = "value="+nextValue
    var mId = Cookies.get('mId');
    var url="/api/title/"+mId+"/rating/user"
    fetch(url, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: sendBody
    })
      .catch(error => console.error(error));
    this.setState({rating: nextValue});
  }

  handleSend(e) {
    var url="/api/friends/request/send";
    console.log("SEND "+Cookies.get('uId'));
    var sendBody = "recipient="+this.state.userName;
    fetch(url, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: sendBody
    })        
    window.location.reload();
  }

  handleAccept(e) {
    var url="/api/friends/respond";
    console.log("RESPOND "+Cookies.get('uId'));
    var sendBody = "state=ACCEPTED&sender="+this.state.userName;
    fetch(url, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: sendBody
    })
    window.location.reload();
  }

  handleUnfriend(e) {
    var url="/api/friends/delete";
    console.log("DELETE "+Cookies.get('uId'));
    var sendBody = "friend="+this.state.userName;  
    fetch(url, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: sendBody
    })
    window.location.reload();
  }

  handleReview(e) {
    var comt = document.getElementById('commentbox').value
    var sendBody = "desc="+comt   
    var mId = Cookies.get('mId');
    var url="/api/title/"+mId+"/review/create"

    fetch(url, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: sendBody
    })
      .then(res => res.json())
      .then(review => console.log("Submitted review "+review))
      .catch(error => console.error(error));    
  }


  render() {
    return(      
      <div class="personal-page">
        <h1 class="text-white">Welcome to personal page of {this.state.userName}</h1>
        <FriendRequest state={this.state} handleAccept={this.handleAccept.bind(this)} handleUnfriend={this.handleUnfriend.bind(this)} handleSend={this.handleSend.bind(this)}/>
        <FriendList fL={this.state.fL} userName={this.state.userName}/>
      </div>
    )
    }
}

function FriendRequest(params) {
  var state = params.state
  console.log("FR u "+Cookies.get('user'));  
  if (Cookies.get('user') !== undefined && Cookies.get('user') !== state.userName) {
    if (state.fL !== [] && state.fL !== undefined && state.fL.filter((user) => { console.log("FR fL "+user+" "+state.userID);
    return (user.sender.id.toString() === state.userID.toString() && user.reciever.username === Cookies.get('user')) || (user.sender.username === Cookies.get('user') && user.reciever.id.toString() === state.userID.toString());}).length > 0) {
      return (
      <button type="button" class="btn btn-primary" onClick={params.handleUnfriend}>Unfriend</button>
      );
    } else if (state.rec !== [] && state.rec !== undefined && state.rec.filter((req) => { 
    console.log("FR REC "+req.sender.username+" "+" "+req.sender.id+" "+req.reciever.id+" "+req.reciever.username);
    return (req.sender.id.toString() === state.userID.toString() && req.reciever.username === Cookies.get('user')) || (req.sender.username === Cookies.get('user') && req.reciever.id.toString() === state.userID.toString());
    }).length > 0) {
      return (
      <button type="button" class="btn btn-primary" onClick={params.handleAccept}>Accept</button>
      );
  }
    
    return (
      <button type="button" class="btn btn-primary" onClick={params.handleSend}>Send Friend Request</button>
    );
  }
  return ( <div/> );
}

function FriendList(params) {
  let fL = params.fL;
  let un = Cookies.get('user');
  if (fL !== undefined) {
    let frl = fL.map((req) => {
        if (req.sender.username === un) {
          return (<div class="text-white">{req.reciever.username}</div>);
        }
        return (<div class="text-white">{req.sender.username}</div>);
      })

    if (fL.length > 0 && params.userName === un) {
      return (
        <div>
        <h4 class="text-white">Your Friendlist</h4>        
        {frl}
        </div>
      );
    }
  }
  return (
    <h4 class="text-white">Your Friendlist</h4> 
  );
}

export default withRouter(User);
