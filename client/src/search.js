import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class SearchBox extends Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(e) {
    e.preventDefault();
    let term = document.getElementById("sbox").value;
    this.props.fetchMovieID(term);
  }

  render() {
    
    return (
      <div className="search-bar">
         <style dangerouslySetInnerHTML={{__html: `
              .search-bar { padding: 15px 10% }
              .searchbox { width: 50%; height: 25px }
              .searchbutton { height: 30px }
            `}} />
        <div className="row">
          <div className="col2">
            <form className="search-form">
                <input id="sbox" className="searchbox" type="text" placeholder="Search Movie"/>
                <button className="searchbutton" onClick={this.handleChange}>Search</button>
              </form>
          </div>
        </div>
      </div>
    )
  }
}
export default SearchBox;
