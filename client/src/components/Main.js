import React from 'react'
import { Switch, Route } from 'react-router-dom'
import Movie from './Movie'
import SearchResult from './result'
import Register from './register'
// The Main component renders one of the three provided
// Routes (provided that one matches). Both the /roster
// and /schedule routes will match any pathname that starts
// with /roster or /schedule. The / route will only match
// when the pathname is exactly the string "/"
const Main = () => (
  <main>
    <Switch>
      <Route exact path='/' component={Movie}/>
      <Route path='/movie/*' component={Movie}/>
      <Route path='/search/*' component={SearchResult}/>
      <Route path='/register' component={Register}/>
      <Route path='*' component={Movie} />
    </Switch>
  </main>
)

export default Main;
