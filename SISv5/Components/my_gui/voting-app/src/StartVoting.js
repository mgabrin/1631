import React, { Component } from 'react';
import './App.css';
var request = require('request');

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
        })
    }

    startVoting(){
        request.post({
            headers: {
                'Content-Type': 'application/json'
            }, 
            url: 'http://localhost:3001/startVoting',
            form: {
                username: this.state.username, 
                password: this.state.password
            }
        }, (err, res, body) => {
            var parsedBody = JSON.parse(body)
            if(parsedBody.Success === 'False' || res.statusCode !== 200){
                alert('Error starting voting. Make sure the username and password are correct.');
            } else {
                this.setState({votingStatus: parsedBody.Status})
                alert('Successfully started voting!');
            }    
        });
    }

    endVoting() {
        request.post({
            headers: {
                'Content-Type': 'application/json'
            }, 
            url: 'http://localhost:3001/endVoting',
            form: {
                username: this.state.username, 
                password: this.state.password
            }
        }, (err, res, body) => {
            var parsedBody = JSON.parse(body)
            if(parsedBody.Success === 'False' || res.statusCode !== 200){
                alert('Error starting voting. Make sure the username and password are correct.');
            } else {
                this.setState({votingStatus: parsedBody.Status});
                alert('Successfully ended voting! Go to the results tab to view the final tally.');
            } 
        });
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
                <h2>Voting Open</h2>
            }
            {!this.state.votingStatus &&
                <h2>Voting Closed!</h2>
            }
        </div>
        );
    }
}

export default StartVoting;