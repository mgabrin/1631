import React, { Component } from 'react';
import './App.css';
var request = require('request');
var _ = require('lodash');
import { startVotingRequest, endVotingRequest } from './Utilities';

const apiUrl = 'http://localhost:3001'

class Test extends Component {
    constructor(props) {
        super(props);
        this.fileChanged = this.fileChanged.bind(this);
    }

    componentWillMount(){
      request.get(apiUrl + '/currentResults', (err, res, body) =>{
          var parsedBody = JSON.parse(body);
          this.setState({candidates: parsedBody.Candidates})
      });
    }

    
    fileChanged(event, that){
        var reader = new FileReader();
        reader.onload = function(){
            var text = reader.result;
            var data = JSON.parse(text)
            _.forEach(data, entry =>{
                entry['Status'] = 'Waiting...'
            });
            console.log(data);
            that.setState({tests: data});
            that.runTests()
        }
        reader.readAsText(event.target.files[0]);
    }

    runTests(){
        console.log('in runTests')
        var data = this.state.tests
        _.forEach(data, test => {
            if(test.action === 'startVoting'){
                startVotingRequest(test.data.username, test.data.password)
                .then(parsedBody => {
                    console.log(parsedBody)
                    if (parsedBody.Success === test.expectedOutcome){
                        test.Status = 'Success'
                        this.setState({tests: data})
                    } else {
                        test.Status = 'Fail'
                        this.setState({tests: data})
                    }
                });
            } else if(test.action === 'endVoting'){
                endVotingRequest(test.data.username, test.data.password)
                .then(parsedBody => {
                   if (parsedBody.Success === test.expectedOutcome){
                        test.Status = 'Success'
                        this.setState({tests: data})
                    } else {
                        test.Status = 'Fail'
                        this.setState({tests: data})
                    } 
                })
            } else if(test.action === 'addPoster'){

            } else if(test.action === 'vote'){

            } else if(test.action === 'getResults'){

            }
        });
    }


    state = {
        file: null,
        votingStatus: false,
        tests: []
    }

    render() {
        var that = this;
        return (
        <div className="App">
            <h1>Test Cases</h1>
            <button className="buttonStyle">
                <label>
                    <input type="file" onClick={(event) => event.target.value = null} 
                        onChange={(event) => this.fileChanged(event, that)}/>
                    Click here to upload file...
                </label>
            </button>
            <br />
             {this.state.tests.map(function(object, i){
                return (
                    <p key={i}>
                        <b>{object.name}: {object.description}</b> 
                        {object.Status === 'Waiting...' && <p>{object.Status}</p>}
                        {object.Status === 'Success' && <p style={{color:'green'}}>{object.Status}</p>}
                        {object.Status === 'Fail' && <p style={{color:'red'}}>{object.Status}</p>}
                    </p>
                ) 
            })}
        </div>
        );
    }
}

export default Test;