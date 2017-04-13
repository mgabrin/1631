import React, { Component } from 'react';
import './App.css';
import { getCurrentResultsRequest } from './Utilities';

class Other extends Component {
    componentWillMount(){
        getCurrentResultsRequest()
        .then(parsedBody => {
            this.setState({candidates: parsedBody.Candidates})
        })
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