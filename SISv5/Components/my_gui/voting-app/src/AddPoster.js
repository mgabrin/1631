import React, { Component } from 'react';
import './App.css';

class AddPoster extends Component {
     constructor(props) {
        super(props);
        this.addPoster = this.addPoster.bind(this);
    }

    addPoster(){
        console.log('add poster');
    }

    state = {
        username: '',
        password: '',
        postername: '',
        creatorGender: '',
        creatorYear: ''
    }

    render() {
        return (
        <div className="App">
            <h1>Add Poster</h1>
            <br />
            <label>Username: <input type="text" name="name" className="inputBox" value={this.state.username} onChange={this.handleUsernameChange}/>
            </label>
            <br />
            <br />
            <label>Password: <input type="text" name="name" className="inputBox" value={this.state.password} onChange={this.handlePasswordChange}/>
            </label>
            <br />
            <br />
            <label>Poster ID: <input type="text" name="name" className="inputBox" value={this.state.password} onChange={this.handlePasswordChange}/>
            </label>
            <br />
            <br />
            <label>Category: <input type="text" name="name" className="inputBox" value={this.state.password} onChange={this.handlePasswordChange}/>
            </label>
            <br />
            <br />
            <label>Creator Gender: <input type="text" name="name" className="inputBox" value={this.state.password} onChange={this.handlePasswordChange}/>
            </label>
            <br />
            <br />
            <label>Creator Year: <input type="text" name="name" className="inputBox" value={this.state.password} onChange={this.handlePasswordChange}/>
            </label>
            <br />
            <br />
            <button className="buttonStyle" onClick={() => this.addPoster()}>Add Poster</button>
        </div>
        );
    }
}

export default AddPoster;