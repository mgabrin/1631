import React, { Component } from 'react';
import './App.css';
import { addPosterRequest } from './Utilities';

var request = require('request')
const apiUrl = 'http://localhost:3001'

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

    componentWillMount(){
        request.get(apiUrl + '/votingStatus', (err, res, body) =>{
            var parsedBody = JSON.parse(body);
            this.setState({votingStatus: parsedBody.Status})
        });
    }

    addPoster(){
        var poster = {
            posterId: this.state.posterId,
            category: this.state.category,
            creatorYear: this.state.creatorYear
        }
        addPosterRequest(this.state.username, this.state.password, poster)
        .then((parsedBody) =>{
            if(parsedBody.Success === 'True'){
                alert('Successfully Added Poster!');
            } else {
                alert('Error adding poster. Make sure username and password are correct and that all necessary fields are filled out.');
            }
        });
    }

    state = {
        votingStatus: false,
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

export default AddPoster;