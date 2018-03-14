import React, { Component } from 'react';
import Login from './login';

class SearchBox extends Component {
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
    document.searchform.action = "/search/"+term.value
  }

  render() {  
    return (
      <div className="search-bar">
         <style dangerouslySetInnerHTML={{__html: `
              .search-bar { padding: 15px 10% }
              .searchbox { width: 50%; height: 40px }
            `}} />
        <div className="row">
            <form    
              name="searchform"
              className="searchbox"
              onSubmit={(e) => this.handleSubmit(e)}
            >
              <span>
              <input id="sbox" className="searchbox" type="text" placeholder="Search Movie"/>
              <button type="button" className="btn btn-primary">Search</button>
              </span>
            </form>
            <Login/>
        </div>
      </div>
    )
  }
}

export default SearchBox;
