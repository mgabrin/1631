import React, { Component } from 'react';
import './App.css';

var request = require('request');

class AddPoster extends Component {
     constructor(props) {
        super(props);
        this.addPoster = this.addPoster.bind(this);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleCategoryChange = this.handleCategoryChange.bind(this);
        this.handleCreatorYearChange = this.handleCreatorYearChange.bind(this);
        this.handleIdChange = this.handleIdChange.bind(this);
    }

    addPoster(){
        request.post({
            headers: {
                'Content-Type': 'application/json'
            }, 
            url: 'http://localhost:3001/addPoster',
            form: {
                username: this.state.username, 
                password: this.state.password,
                posterId: this.state.posterId,
                category: this.state.category,
                year: this.state.creatorYear
            }
        }, (err, res, body) => {
            if(body.Success === 'False' || res.statusCode !== 200){
                alert('Error adding poster. Make sure username and password are correct and that all necessary fields are filled out.');
            } else {
                alert('Successfully Added Poster!');
            } 
        });
    }

    state = {
        username: '',
        password: '',
        posterId: '',
        category: '',
        creatorYear: ''
    }

    handleUsernameChange(event){
        this.setState({username: event.target.value});
    }

    handlePasswordChange(event){
        this.setState({password: event.target.value});
    }

    handleIdChange(event){
        this.setState({posterId:event.target.value});
    }

    handleCreatorYearChange(event){
        this.setState({creatorYear: event.target.value});
    }

    handleCategoryChange(event){
        this.setState({category: event.target.value});
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
            <label>Password: <input type="password" name="name" className="inputBox" value={this.state.password} onChange={this.handlePasswordChange}/>
            </label>
            <br />
            <br />
            <hr width="50%" />
            <br />
            <label>Poster ID: <input type="text" name="name" className="inputBox" value={this.state.posterId} onChange={this.handleIdChange}/>
            </label>
            <br />
            <br />
            <label>Category: <input type="text" name="name" className="inputBox" value={this.state.category} onChange={this.handleCategoryChange}/>
            </label>
            <br />
            <br />
            <label>Creator Year: <input type="text" name="name" className="inputBox" value={this.state.creatorYear} onChange={this.handleCreatorYearChange}/>
            </label>
            <br />
            <br />
            <button className="buttonStyle" onClick={() => this.addPoster()}>Add Poster</button>
        </div>
        );
    }
}

export default AddPoster;