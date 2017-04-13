import React, { Component } from 'react';
import './App.css';
import StartVoting from './StartVoting';
import AddPoster from './AddPoster';
import Other from './Other';
import Vote from './Vote';
import Test from './Test';

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
          <button className="tablinks" onClick={ () => this.showTab('vote')}>Vote</button>
          <button className="tablinks" onClick={ () => this.showTab('test')}>Test</button>

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
        {this.state.tabName === 'vote' &&
          <Vote />
        }
        {this.state.tabName === 'test' &&
          <Test />
        }
      </div>
    );
  }
}

export default App;
