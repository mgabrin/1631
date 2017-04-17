import React, { Component } from 'react';
import './App.css';
var request = require('request');
import { startVotingRequest, endVotingRequest } from './Utilities';

const apiUrl = 'http://localhost:3001'

class StartVoting extends Component {
    constructor(props) {
        super(props);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
    }

    componentWillMount(){
        request.get(apiUrl + '/votingStatus', (err, res, body) =>{
            var parsedBody = JSON.parse(body);
            this.setState({votingStatus: parsedBody.Status})
        });
    }

    startVoting(){
        startVotingRequest(this.state.username, this.state.password)
        .then(parsedBody =>{
            if(parsedBody.Success === 'True'){
                this.setState({votingStatus: parsedBody.Status})
                alert('Successfully started voting!');
            } else {
                alert('Error starting voting. Make sure the username and password are correct.');
            }
        });
        
    }

    endVoting() {
        var r = confirm("Are you sure you want to end voting?")
        if (r){
            endVotingRequest(this.state.username, this.state.password)
            .then(parsedBody => {
                if(parsedBody.Success === 'True'){
                    this.setState({votingStatus: parsedBody.Status})
                    alert('Successfully ended voting!');
                } else {
                    alert('Error ending voting. Make sure the username and password are correct.');
                }
            });
        }
        
    }

    handleUsernameChange(event){
        this.setState({username: event.target.value})
    }

    handlePasswordChange(event){
        this.setState({password: event.target.value})
    }

    state = {
        username: '',
        password: '',
        votingStatus: false
    }

    render() {
        return (
        <div className="App">
            <h1>Handle Voting Status</h1>
            <br />
            <label>Username: <input type="text" name="name" className="inputBox" value={this.state.username} onChange={this.handleUsernameChange}/>
            </label>
            <br />
            <br />
            <label>Password: <input type="password" name="name" className="inputBox" value={this.state.password} onChange={this.handlePasswordChange}/>
            </label>
            <br />
            <br />
            <button className="buttonStyle" disabled={this.state.votingStatus} onClick={() => this.startVoting()}>Start Voting</button>
            <br />
            <br />
            <button className="buttonStyle" disabled={!this.state.votingStatus} onClick={() => this.endVoting()}>End Voting</button>
            <br />
            {this.state.votingStatus &&
                <h2>Voting Open!</h2>
            }
            {!this.state.votingStatus &&
                <h2>Voting Closed! Restart server to ensure data integrity.</h2>
            }
        </div>
        );
    }
}

export default StartVoting;