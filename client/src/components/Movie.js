import React, { Component } from 'react';

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
      movies: []
    }
    this.api = "53f856f34ff5b6efc67de7e14ac5617d"
  }

  componentDidMount() {
    let url = `https://api.themoviedb.org/3/movie/${this.state.movieID}?&api_key=${this.api}`
    this.fetchApi(url)
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
        
      })
    })
  }

  render() {
    return(      
      <MetaData movie={this.state}/>
      )
    }
}

function MetaData(params) {
    let reviews = []
    let movie = params.movie
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

    if (movie.reviews === undefined) {
      reviews = <span className='no-review'>No review yet!</span>
    } else {
      reviews = reviews.map((review, ii) => { 
        return <div id={ii}>{review}</div>;
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
              </div>
            </div>
          </div>
        </div>
        <br/>
        <div className="review-container">
          <div>{reviews}</div>
        </div>
      </div>
  );
}
export default Movie;
