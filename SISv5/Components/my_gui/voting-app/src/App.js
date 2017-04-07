import React, { Component } from 'react';
import './App.css';
import StartVoting from './StartVoting';
import AddPoster from './AddPoster';
import Other from './Other';

class App extends Component {
  constructor(props) {
    super(props);
    this.showTab = this.showTab.bind(this);
  }

  state = {
    tabName: 'startVoting'
  }
  
  showTab = (tabName) =>{
    this.setState({tabName: tabName})
  }

  render() {
    return (
      <div className="App">
        <div className="tab">
          <button onClick={() => this.showTab('startVoting')}>Handle Voting Status</button>
          <button className="tablinks" onClick={() => this.showTab('addPoster')}>Add Poster</button>
          <button className="tablinks" onClick={ () => this.showTab('other')}>Get Results</button>
        </div>
        {this.state.tabName === 'startVoting' && 
          <StartVoting />
        }
        {this.state.tabName === 'addPoster' &&
          <AddPoster />
        }
        {this.state.tabName === 'other' && 
          <Other />
        }
      </div>
    );
  }
}

export default App;
