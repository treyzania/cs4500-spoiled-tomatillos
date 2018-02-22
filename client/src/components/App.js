import React, { Component } from 'react';
import SearchBox from './search';
import Main from './Main';

class App extends Component {
  render() {
    return (
      <div>
        <SearchBox />
        <Main />
      </div>
    )
  }
}

export default App;
