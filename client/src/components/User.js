import React, { Component } from 'react';
import StarRatingComponent from 'react-star-rating-component';
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
      .then((resp) => return resp.json())
      .then((data) => {console.log("CUR US "+data); this.setState({userName: data.username })})
      .catch((error) => console.log(error));
//logged in
    if (Cookies.get('user') !== undefined) {
      fetch("/api/friends/list")
        .then((resp) => return resp.json())
        .then((data) => {console.log("FList "+data); this.setState({ fL: data});})
        .catch((error) => console.error(error));
      fetch("/api/friends/received")
        .then((resp) => return resp.json())
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
    url="/api/friends/request/send";
    console.log(Cookies.get('uId');
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
    url="/api/friends/respond";
    console.log(Cookies.get('uId');
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
    url="/api/friends/delete";
    console.log(Cookies.get('uId');
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

function Review(params) {
  var user = params.user
  var desc = params.desc
  return ( 
    <div className="panel panel-default">
      <div className="panel-body">{desc}</div>
      <div className="panel-body text-right bold">{user}</div>
    </div>
  );
}

function ReviewBox(params) {
  if (Cookies.get('user') !== undefined) {
    return (
      <form onSubmit={params.handleReview} className="mx-auto px-5" style={{width: '80%'}}>
      <textarea id='commentbox' row="4" col="50" name="comment" form="usrform" placeholder="Your comment" className="w-75"/>
      <br/>
      <button type="submit" className="btn btn-default">Comment</button>
      </form>
    );
  }
  return (
    <div/>
  );
}

function FriendRequest(params) {
  if (Cookies.get('user') !== undefined) {
    if (this.state.fL !== [] && this.state.fL !== undefined && this.state.fL.filter((user) => { console.log("FR fL "+user.id+" "+this.state.userID);
    return user.id == this.state.userID;}).length > 0) {
      return (
      <button type="button" class="btn btn-primary" onClick={params.handleUnfriend}>Unfriend</button>
      );
    } else if (this.state.rec !== [] && this.state.rec !== undefined && this.state.rec.filter((user) => { 
    console.log("FR fL "+user.id+" "+this.state.userID);
    return user.id == this.state.userID;
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

function MetaData(params) {
    let reviews = []
    let movie = params.movie
    let imdbVote = params.movie.imdbVote
    let rating = params.movie.rating
    let posterIMG = 'https://image.tmdb.org/t/p/w500' + movie.poster,
        genres = [],
        empty = '-'
    
    if (movie.genre !== undefined) {
      genres = movie.genre.map((gen, ii) => { return <span class="text-success flex-wrap" key={ii}>{gen.name+" "}</span>;});
    }

    if (movie.vote === undefined || movie.vote === 0) {
      movie.vote = empty
    } else if (movie.vote.toString().indexOf("/") === -1) {
        movie.vote = movie.vote
    };

    if (movie.reviews === [] || movie.reviews === undefined || movie.reviews.status === 400) {
      reviews = <Review user="None" desc="No Review yet!"/>
    } else {
      console.log(movie.reviews);
      reviews = movie.reviews.map((review, ii) => { 
        console.log("Rendering rev "+ review);
        return <Review id={ii} user={review.user.username} desc={review.description}/>;
      });
    }

    return (
      <div className="movie-page">
        <div className="movie-data">
          <div className="poster-container">
            <img className="poster" src={posterIMG} alt={movie.original_title}/>
          </div>
          <div className="meta-data-container">
            <h1>{movie.original_title}</h1>
            <hr size="2"/>
            <span className="text-danger">{movie.tagline}</span>
            <br/>
            <h3 className="">Genre</h3>
            <div>{genres}</div>
            <h3>Plot</h3>
            <p>{movie.overview}</p>            
            <div className="additional-details">
              <div>
                <h4> Release: <span className="text-success">{movie.release}</span></h4>
                <h4> Running Time: <span className="text-success">{movie.runtime} mins</span> </h4>
                <br/>
                <h4>Rating</h4>
                <MovieRating ratings={imdbVote}/>
                <hr size="2"/>
                <StarRating rating={rating} onStarClick={params.onStarClick}/>
                <div className="review-container">
                  <div>{reviews}</div>
                </div>
                <ReviewBox handleReview={params.handleReview}/>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
}

export default withRouter(User);
