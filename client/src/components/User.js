import React, { Component } from 'react';
import Cookies from 'js-cookie';
import { withRouter } from 'react-router-dom';

class User extends Component {
  constructor(props) {
    super(props)
    let match = this.props.match
    let id= -1
    if (match !== undefined && match.url !== "/") {
      let search = match.url;
      id = search.substring(7, search.length);
    }
    this.state = {
      userID: id, // 106646
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
      fetch("/api/friends/list")
        .then((resp) => resp.json())
        .then((data) => {console.log("FList "+data); this.setState({ fL: data});})
        .catch((error) => console.error(error));
      fetch("/api/friends/received")
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
  }

  handleAccept(e) {
    var url="/api/friends/respond";
    console.log("RESPOND "+Cookies.get('uId'));
    var sendBody = "state=ACCEPTED&sender="+Cookies.get('user');
    fetch(url, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: sendBody
    })
        
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
    console.log("render "+this.state.imdbVote);
    return(      
      <div>
        <h1>Welcome to personal page of {this.state.userName}</h1>
        <FriendRequest handleAccept={this.handleAccept} handleUnfriend={this.handleUnfriend} handleSend={this.handleSend}/>
      </div>
    )
    }
}


function FriendRequest(params) {
  if (Cookies.get('user') !== undefined) {
    if (this.state.fL !== [] && this.state.fL !== undefined && this.state.fL.filter((user) => { console.log("FR fL "+user.id+" "+this.state.userID);
    return user.id === this.state.userID;}).length > 0) {
      return (
      <button type="button" class="btn btn-primary" onClick={params.handleUnfriend}>Unfriend</button>
      );
    } else if (this.state.rec !== [] && this.state.rec !== undefined && this.state.rec.filter((user) => { 
    console.log("FR fL "+user.id+" "+this.state.userID);
    return user.id === this.state.userID;
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


export default withRouter(User);
