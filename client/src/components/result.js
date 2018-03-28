import React, { Component } from 'react';
import "./style.css";
import Cookies from 'js-cookie';

class SearchResult extends Component {
  constructor(props) {
    super(props)
    let match = this.props.match
    let term = ""
    if (match !== undefined && match.url !== "") {
      let search = match.url;
      term = search.substring(8, search.length);
    }
    this.state = {
      title: term,
      movies: []
    }
    this.api = "53f856f34ff5b6efc67de7e14ac5617d"
  }

  componentDidMount() {
    let url = `https://api.themoviedb.org/3/search/movie?api_key=${this.api}&language=en-US&query=${this.state.title}&include_adult=false`;
    fetch(url).then((res) => res.json()).then((data) => {
        this.setState({
          movies: data.results.map((movie) => {
            return { title: movie.title, id: movie.id, poster_path: movie.poster_path }
          })
        })
    })
    Cookies.remove('mId');
  }

  render() {
    if (this.state.movies === [] || this.state.movies === undefined || this.state.movies.length === 0) {
      return (
        <h1>Sorry! We cant find what you are looking for.</h1>
      );
    }
    return (
      <Movies movies={this.state.movies}/>
    );
  }  
}

function Movies(params) {
  let movies = params.movies
  let posterUrl = 'https://image.tmdb.org/t/p/w500';

  let moviesSet = movies.map((movie) => {
    return <Movie key={movie.id} id={movie.id} poster={posterUrl+movie.poster_path} title={movie.title} />
  });

  return (
    <div className="movies">
      {moviesSet}
    </div>
  );
}

function Movie(params) {
  let id= params.id
  let title= params.title
  let poster= params.poster
  let url="/movie/"+id
  return (
    <div id={id} className="poster-container">
      <figure>
        <a href={url}>
          <img className="poster" src={poster} alt={title}/>       
          <figcaption>{title}</figcaption>
        </a>
      </figure>
    </div>
  );
}

export default SearchResult;
