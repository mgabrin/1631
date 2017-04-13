import React, { Component } from 'react';
import './App.css';
var request = require('request');

const apiUrl = 'http://localhost:3001'

class Other extends Component {
    componentWillMount(){
      request.get(apiUrl + '/currentResults', (err, res, body) =>{
          console.log('component mounted')
          var parsedBody = JSON.parse(body);
          console.log(body)
          this.setState({candidates: parsedBody.Candidates})
      });
    }


    state = {
        votingStatus: false,
        candidates: [
          {'name': 'hello', 'total': 0}, 
          {'name':'world', 'total':0}
        ],
    }

    render() {
        return (
        <div className="App">
            <h1>Vote</h1>

            {this.state.candidates.map(function(object, i){
                return <p key={i}><b>{object.name}</b> : {object.total}</p> 
            })}

            {this.state.votingStatus &&
                <h2>Voting Open</h2>
            }
            {!this.state.votingStatus &&
                <h2>Voting Closed</h2>
            }
        </div>
        );
    }
}

export default Other;