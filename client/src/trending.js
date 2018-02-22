import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class Trending extends Component {
  render() {
    let trending_movies = [{id: 1, name: "Wolf Of WallStreet"}].map((movie, ii) => { return <div className="trending-movie">{movie.name}</div>})

    return (
      <div className="trending-page">
        <div className="trending-data">
          <style dangerouslySetInnerHTML={{__html: `
            .trending-page { float: left; padding: 15px; border: thin solid black; width: 60%; height: 500px; margin: 10px }
            .trending-title { font-size: 20px; border: thin solid black; margin: 20px }
            .movie-list { display: flex; width: 80%; margin: auto }
          `}} />
          <div className="trending-container">
            <div className="trending-title">Trending Today</div>
            <div className="movie-list">{trending_movies}</div>
          </div>
        </div>
      </div>
    )
  }
}

export default Trending;
