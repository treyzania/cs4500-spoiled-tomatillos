import React, { Component } from 'react';
import "./style.css";
import Cookies from 'js-cookie';
import { withRouter } from 'react-router-dom';

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
            return { title: movie.title, id: movie.id, poster_path: movie.poster_path, overview: movie.overview, genre: movie.genre }
          })
        })
    })
    Cookies.remove('mId');
  }

  render() {
    if (this.state.movies === [] || this.state.movies === undefined || this.state.movies.length === 0) {
      return (
        <h1 class="text-white">Sorry! We cant find what you are looking for.</h1>
      );
    }
    return (
      <Movies movies={this.state.movies} history={this.props.history}/>
    );
  }  
}

function Movies(params) {
  let movies = params.movies
  let posterUrl = 'https://image.tmdb.org/t/p/w500';
  let history = params.history
  let moviesSet = movies.map((movie) => {
    return <Movie key={movie.id} id={movie.id} poster={posterUrl+movie.poster_path} title={movie.title} genre={movie.genre} desc={movie.overview} history={history}/>
  });

  return (
    <div class="content">
      <div class="container">
        <div class="row">
          <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 mb40 text-center">
            <h1 class="text-white">Search Result</h1>                
          </div>
        </div>
        <div class="row">
          {moviesSet}
        </div>
        <div class="row">
         <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 text-center">
           Data taken from TMDB
         </div>
        </div>
      </div>
    </div>
  );
}

function Movie(params) {
  let id= params.id
  let title= params.title
  let poster= params.poster
  let url="/movie/"+id
  let genre=params.genre
  let desc = params.desc;
  if (params.desc.length >= 500) {
    desc = params.desc.substring(0,600);
  }

  function handleClick(e) {
    params.history.push(url);
  };
  
  return (
    <div class="col-xl-4 col-lg-4 col-md-6 col-sm-12 col-12">
      <div class="team-block">
        <div class="team-img">
          <img src={poster} alt={title} onClick={handleClick}/>
          <div class="overlay" onClick={handleClick}>
            <div class="text">
              <h4 class="mb0 text-white underline">{title}</h4>
              <h6 class="mb30 team-meta">{genre}</h6>
              <p>{desc}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default withRouter(SearchResult);
