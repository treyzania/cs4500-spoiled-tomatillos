import React, { Component } from 'react';
import StarRatingComponent from 'react-star-rating-component';
import Cookies from 'js-cookie';

class Movie extends Component {
  constructor(props) {
    super(props)
    let match = this.props.match
    let id= 106646
    if (match !== undefined && match.url !== "/") {
      let search = match.url;
      id = search.substring(7, search.length);
    }
    this.state = {
      movieID: id, // 106646
      movies: [],
      rating: 0,
      imdbVote: [],
    }
    this.api = "53f856f34ff5b6efc67de7e14ac5617d"
  }

  componentDidMount() {
    let url = `https://api.themoviedb.org/3/movie/${this.state.movieID}?&api_key=${this.api}`
    this.fetchApi(url)
    if (Cookies.get('rating') !== "" && Cookies.get('rating') !== undefined) {
      this.setState({rating: Cookies.get('rating')});
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

  async fetchApi(url) {
    await fetch(url).then((res) => res.json()).then((data) => {
      this.setState({        
          movieID: data.id,
          original_title: data.original_title,
          tagline: data.tagline,
          overview: data.overview,
          homepage: data.homepage,
          poster: data.poster_path,
          production: data.production_companies,
          production_countries: data.production_countries,
          genre: data.genres,
          release: data.release_date,
          vote: data.vote_average,
          runtime: data.runtime,
          revenue: data.revenue,
          backdrop: data.backdrop_path
        
      });
      var sendBody = "name="+data.original_title
      fetch("/api/title/by-name?"+sendBody)
      .then((resp) => resp.json())
      .then((title) => {
        console.log("By name "+title);
        if (title.id !== undefined) {
          Cookies.set('mId', title.id);
        } else {
          this.createMovie(data.original_title, 2017, data.original_title);
        }
      })
      .catch(error => {
        console.error("Title by name "+error)
        this.createMovie(data.original_title, 2017, data.original_title);
       });
      fetch(`http://www.omdbapi.com/?t=${data.original_title}&apikey=1c264519`)
        .then((resp) => resp.json())
        .then((data) => {
          console.log(data.Ratings);
          this.setState({ imdbVote: data.Ratings });
        })
        .catch(error => {
          console.error("IMDB "+error)
        });        
    });
    var mId = Cookies.get('mId')
    fetch("/api/title/"+mId+"/review/all").then((res) => res.json()).then((reviewsData) => {
      if (reviewsData !== undefined && reviewsData !== []) {
        this.setState({
          reviews: reviewsData
        });
        console.log("Reviews: "+reviewsData[0].description);
      }
    })
    .catch(error => console.error(error));
    fetch("/api/title/"+mId+"/ratings/all").then((res) => res.json()).then((reviewsData) => {
      console.log("Rating: "+reviewsData[0]);
      if (reviewsData !== [] && Cookies.get('id') !== undefined) {
        var uId = Cookies.get('id');
        var rate = reviewsData.filter((review) => {
          console.log(review.user+" "+uId);
          return review.user.toString() === uId;
        });
        this.setState({ rating: rate[0].rating});
      }
    })
    .catch(error => console.error(error));
        
  }

  async createMovie(name, year, desc) {
    var url = "/api/title/create";
    var sendBody = "name="+name+"&year="+year+"&desc="+desc;
    await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: sendBody
    })
      .then((res) => res.json())
      .then((title) => {
        console.log("Created "+title);
        Cookies.set('mId', title.id);
      })
      .catch(error => console.log(error))
 }

  render() {
    console.log("render "+this.state.imdbVote);
    return(      
      <MetaData movie={this.state} onStarClick={this.onStarClick.bind(this)} handleReview={this.handleReview.bind(this)}/>
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

function StarRating(params) {
  if (Cookies.get('user') !== undefined) {
    return (
      <div>
        <div> Your Rating </div>
        <StarRatingComponent 
          name="rate1" 
          starCount={10}
          value={params.rating}
          onStarClick={params.onStarClick}
      />
      </div>
    );
  }
  return ( <div/> );
}

function MovieRating(params) {
  console.log("MR "+params.ratings);
  let ratings = params.ratings.map((rating) => {
    return (      
      <div class="col-12">
      <div>{rating.Source}</div>
      <div>{rating.Value}</div>
      </div>
    );
  });
  return (
    <div>{ratings}</div>
  );
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
            <h1 class="bold">{movie.original_title}</h1>
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

export default Movie;
