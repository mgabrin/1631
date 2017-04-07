import React, { Component } from 'react';
import './App.css';

class StartVoting extends Component {
    constructor(props) {
        super(props);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
    }
    startVoting(){
        console.log(this.state.password);
    }

    endVoting() {
        console.log(this.state.password);
    }

    handleUsernameChange(event){
        this.setState({username: event.target.value})
    }

    handlePasswordChange(event){
        this.setState({password: event.target.value})
    }

    state = {
        username: '',
        password: ''
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
            <label>Password: <input type="text" name="name" className="inputBox" value={this.state.password} onChange={this.handlePasswordChange}/>
            </label>
            <br />
            <br />
            <button className="buttonStyle" onClick={() => this.startVoting()}>Start Voting</button>
            <br />
            <br />
            <button className="buttonStyle" onClick={() => this.endVoting()}>End Voting</button>
        </div>
        );
    }
}

export default StartVoting;