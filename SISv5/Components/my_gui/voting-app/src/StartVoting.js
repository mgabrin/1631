import React, { Component } from 'react';
import './App.css';
import { 
    socketConnect, 
    socketProvider, 
} from 'socket.io-react';
import io from 'socket.io-client';
import ws from 'ws';

class StartVoting extends Component {
    constructor(props) {
        super(props);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
    }
    socket = null
    ws = new Websocket('127.0.0.1:53217')

    componentWillMount(){
        conn.addPair("Scope", "SIS.Scope1");
        conn.addPair("MessageType", "Connect");
        conn.addPair("Role", "Basic");
        conn.addPair("Name", "InputProcessor");
        ws.send('(Scope$$$SIS.Scope1$$$MessageType$$$Connect$$$Role$$$Basic$$$Name$$$GUI$$$)')
    }

    startVoting(){
        console.log("Sent");
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