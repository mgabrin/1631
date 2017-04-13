import React, { Component } from 'react';
import './App.css';
var request = require('request');

const apiUrl = 'http://localhost:3001'

class Vote extends Component {
    constructor(props) {
        super(props);
        this.handleVote = this.handleVote.bind(this);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
    }

    componentWillMount(){
        request.get(apiUrl + '/votingStatus', (err, res, body) =>{
            var parsedBody = JSON.parse(body);
            this.setState({votingStatus: parsedBody.Status})
        })

        request.get(apiUrl + '/currentResults', (err, res, body) => {
            console.log(body)
            var parsedBody = JSON.parse(body);
            this.setState({candidates: parsedBody.Candidates})
        })

    }

    handleVote(){
        console.log('vote')
        request.post({
            headers: {
                'Content-Type': 'application/json'
            }, 
            url: 'http://localhost:3001/vote',
            form: {
                username: this.state.username, 
                vote: this.state.vote
            }
        }, (err, res, body) => {
            var parsedBody = JSON.parse(body)
            if(parsedBody.Success === 'False' || res.statusCode !== 200){
                alert('Error! ' + parsedBody.Message);
            } else {
                alert('Successful Vote!');
            } 
        });
    }

    handleSelection(object){
        console.log('handle selection');
        console.log(object)
        this.setState({vote: object})
    }

    handleUsernameChange(event){
        this.setState({username: event.target.value})
    }

    state = {
        votingStatus: false,
        candidates: ['hello', 'world'],
        vote: null,
        username: ''
    }

    render() {
        var that = this
        return (
        <div className="App">
            <h1>Vote</h1>
            <label>Name: <input type="text" name="name" className="inputBox" value={this.state.username} onChange={this.handleUsernameChange}/>
            </label>
            <br />
            <br />
            {this.state.candidates.map(function(object, i){
                return <p key={i}><input type="radio" onChange={() => that.handleSelection(object)} name="candidate"/>&#09;{object.name}</p> 
            })}
            
            <button className="buttonStyle" disabled={!this.state.votingStatus} onClick={() => this.handleVote()}>Vote</button>
            <br />
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

export default Vote;