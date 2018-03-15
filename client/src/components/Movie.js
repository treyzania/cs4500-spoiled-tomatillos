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
      rating: 0
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
    var comt = "desc="+document.getElementById('commentbox').value
    var mId = Cookies.get('mId')
    var url="/api/title/"+mId+"/review/create"

    fetch(url, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: comt
    })
      .catch(error => console.error(error));
  }

  fetchApi(url) {
    fetch(url).then((res) => res.json()).then((data) => {
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
          Cookies.set('mId', title.id)
        } else {
          this.createMovie(data.original_title, 2017, data.overview);
        }
      })
      .catch(error => {
        console.error("Title by name "+error)
        this.createMovie(data.original_title, 2017, data.overview);
       });
    });

    var mId = Cookies.get('mId');
    fetch("/api/title/"+mId+"/review/all").then((res) => res.json()).then((reviewsData) => {
      this.setState({
        reviews: reviewsData
      });
      console.log("Reviews: "+reviewsData);
    })
    .catch(error => console.error(error));
    fetch("/api/title/"+mId+"/ratings/all").then((res) => res.json()).then((reviewsData) => {
      console.log("Rating: "+reviewsData.rating);
      reviewsData.filter(rating => rating.id === Cookies.get('id'));
      if (reviewsData !== []) {
        this.setState({ rating: reviewsData[0].rating});
      }
    })
    .catch(error => console.error(error));
  }

  createMovie(name, year, desc) {
    var url = "/api/title/create";
    var sendBody = "name="+name+"&year="+year+"&desc="+desc;
    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: sendBody
    })
      .then((res) => res.json())
      .then((title) => {
        console.log("Created "+title);
        Cookies.set('mId',title.id);
      })
      .catch(error => console.log(error))
  }

  render() {
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

function MetaData(params) {
    let reviews = []
    let movie = params.movie
    let rating = params.movie.rating
    let posterIMG = 'https://image.tmdb.org/t/p/w500' + movie.poster,
        genres = [],
        empty = '-'
    
    if (movie.genre !== undefined) {
      genres = movie.genre.map((gen, ii) => { return <span key={ii}>{gen.name+" "}</span>;});
    }

    if (movie.vote === undefined || movie.vote === 0) {
      movie.vote = empty
    } else if (movie.vote.toString().indexOf("/") === -1) {
        movie.vote = movie.vote + ' / 10'
    };

    if (movie.reviews === [] || movie.reviews === undefined) {
      reviews = <Review user="None" desc="No Review yet!"/>
    } else {
      reviews = reviews.map((review, ii) => { 
        return <Review id={ii} user={review.user} desc={review.desc}/>;
      });
    }

    return (
      <div className="movie-page">
        <div className="movie-data">
          <style dangerouslySetInnerHTML={{__html: `
            .poster-container { float: left; padding: 15px; border: thin solid black; width: 30% }
            .poster { max-width: 100%; max-height: 100% }
            .movie-data { border: thin solid black; display: flex; width: 80%; margin: auto }
            .meta-data-container { width: 70%; padding: 1rem; border: thin solid black }
            .meta-data { font-size: 15px; color: red }
            .review-container { padding: 1rem; border: thin solid black; margin: auto; width: 80% }
          `}} />
          <div className="poster-container">
            <img className="poster" src={posterIMG} alt={movie.original_title}/>
          </div>
          <div className="meta-data-container">
            <h1>{movie.original_title}</h1>
            <span className="tagline">{movie.tagline}</span>
            <p>{movie.overview}</p>
            <div className="more-meta">
              <div className="genre-list">Genre:{genres}</div>
              <div>
                <div> Release: <span className="meta-data">{movie.release}</span></div>
                <div> Running Time: <span className="meta-data">{movie.runtime} mins</span> </div>
                <div> Site Score: <span className="meta-data">{movie.vote}</span></div>
                <StarRating rating={rating} onStarClick={params.onStarClick}/>
              </div>
            </div>
          </div>
        </div>
        <br/>
        <div className="review-container">
          <div>{reviews}</div>
        </div>
        <ReviewBox handleReview={params.handleReview}/>
      </div>
  );
}

export default Movie;
