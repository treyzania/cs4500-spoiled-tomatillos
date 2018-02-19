import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class MetaData extends Component {
  render() {
    let reviews = []
    let data = this.props.data
      let posterIMG = 'https://image.tmdb.org/t/p/w500' + data.poster,
          production = data.production,
          genres = [],
          productionList = [],
          empty = '-',
          backdropIMG = 'https://image.tmdb.org/t/p/original' + data.backdrop;

      if (data.genre != undefined) {
        genres = data.genre.map((gen, ii) => { return <span key={ii}>{gen.name}</span>;});
      }
  
      if (production != undefined) {
        console.log(data);
        productionList = production.map((prod, ii) => { return <span key={ii}>{prod.name}</span>});
      }

      if (data.vote === undefined || data.vote === 0) {
        data.vote = empty
      } else {
        data.vote = data.vote + ' / 10'
      };

      if (data.reviews === undefined) {
        reviews = <span className='no-review'>No review yet!</span>
        console.log('No review');
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
              <img className="poster" src={posterIMG}/>
            </div>

            <div className="meta-data-container">
              <h1>{data.original_title}</h1>

              <span className="tagline">{data.tagline}</span>
              <p>{data.overview}</p>
              <div className="more-meta">
                <div className="genre-list">Genre:{genres}</div>
                <div className="production-list">Production:{productionList}</div>
                <div>
                  <div> Release: <span className="meta-data">{data.release}</span></div>
                  <div> Running Time: <span className="meta-data">{data.runtime} mins</span> </div>
                  <div> Site Score: <span className="meta-data">{data.vote}</span></div>
                </div>
              </div>
            </div>
          </div>
          <br/>
          <div className="review-container">
            <div>{reviews}</div>
          </div>
        </div>
      )
    }
}


function nestedDataToString(nestedData) {
  let nestedArray = [],
      resultString;
  nestedArray.forEach(function(item, i){
    nestedArray.push(item.name);
  });
  resultString = nestedArray.join(', '); // array to string
  return resultString;
};

export default MetaData;
