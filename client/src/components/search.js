import React, { Component } from 'react';
import Login from './login';
import { withRouter } from 'react-router-dom';
import Cookies from 'js-cookie';

class SearchBox extends Component {
  constructor(props) {
    super(props);
  }
  fetchMovieTitles(title) {
    let url = `https://api.themoviedb.org/3/search/movie?api_key=${this.api}&language=en-US&query=${title}&include_adult=false`
    fetch(url).then((res) => res.json()).then((data) => {
      this.setState({
        movies:  data.results.map((movie, ii) => {
          return {title: movie.title, id: movie.id, poster_path: movie.poster_path};
      })
    });
    })
  }

  handleSubmit(e) {
    var term = document.getElementById('sbox')
    this.props.history.push('/search/'+term.value);
  }

  handleClick(e) {
    let home = "/"
    if (Cookies.get('id') !== undefined) {
      home = "/user/"+Cookies.get('id');
    }
    this.props.history.push(home);
  }

  handleHome(e) {
    this.props.history.push("/");
  }

  render() {
    return (
<nav class="navbar navbar-expand-lg navbar-dark bg-dark justify-content-between">
  <a class="navbar-brand" href="/" onClick={this.handleHome.bind(this)}>Home</a>
  <a class="navbar-brand" href="/" onClick={this.handleClick.bind(this)}>Your Page</a>
  <form class="form-inline" onSubmit={(e) => this.handleSubmit(e)} name="searchform">
    <input class="form-control mr-sm-2" type="text" id="sbox" placeholder="Search a movie..." aria-label="Search"/>
    <button class="btn btn-primary btn-outline-success my-2 my-sm-0" type="submit">Search</button>
  </form>
  <Login/> 
</nav>

    )
  }
}

export default withRouter(SearchBox);
