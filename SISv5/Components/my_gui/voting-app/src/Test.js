import React, { Component } from 'react';
import './App.css';
var request = require('request');
var _ = require('lodash');
import each from 'async/each';
var diff = require('deep-diff').diff;
import { 
    startVotingRequest, 
    endVotingRequest,
    addPosterRequest,
    getCurrentResultsRequest,
    voteRequest
} from './Utilities';

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
            try{
                var data = JSON.parse(text)
                _.forEach(data, entry =>{
                    entry['Status'] = 'Waiting...'
                });
                that.setState({tests: data});
                that.runTests()
            } catch(e){
                alert('There was a problem opening the file!');
            }
        }
        reader.readAsText(event.target.files[0]);
    }

    runTests(){
        var data = this.state.tests
        console.log(data)
        each(data, (test, callback) => {
            //Test for startVoting returns Success on successful start and Fail on failed start
            if(test.action === 'startVoting'){
                startVotingRequest(test.data.username, test.data.password)
                .then(parsedBody => {
                    console.log('startVoting')
                    if (parsedBody.Success === test.expectedOutcome){
                        test.Status = 'Success'
                    } else {
                        test.Status = 'Fail'
                    }
                    this.setState({tests: data})
                });
            //Test for end voting returns Success on successful end and Fail on failed end
            } else if(test.action === 'endVoting'){
                setTimeout(() => {
                    endVotingRequest(test.data.username, test.data.password)
                    .then(parsedBody => {
                        console.log('end voting')
                    if (parsedBody.Success === test.expectedOutcome){
                            test.Status = 'Success'
                        } else {
                            test.Status = 'Fail'
                        } 
                        this.setState({tests: data})
                    });
                }, 1000);
            //test addPoster returns Success on successfully added poster and Fail on failed alled
            } else if(test.action === 'addPoster'){
                addPosterRequest(test.data.user, test.data.password, test.data.poster)
                .then(parsedBody => {
                    console.log('add poster')
                    if (parsedBody.Success === test.expectedOutcome){
                        test.Status = 'Success'
                    } else {
                        test.Status = 'Fail'
                    }
                    this.setState({tests: data})
                })
            //Vote test returns Success on successful vote and Fail
            } else if(test.action === 'vote'){
                voteRequest(test.data.user, test.data.vote)
                .then(parsedBody => {
                    console.log('vote')
                    if (parsedBody.Message === test.expectedOutcome){
                        test.Status = 'Success'
                    } else {
                        test.Status = 'Fail'
                    }
                    this.setState({tests: data})
                });
            } else if(test.action === 'getResults'){
                setTimeout(() =>{
                    getCurrentResultsRequest()
                    .then(parsedBody => {
                        console.log('results')
                        var differences = diff(parsedBody.Candidates, test.expectedOutcome.Candidates);
                        if (!differences){
                            test.Status = 'Success';
                        } else {
                            test.Status = 'Fail';
                        }
                        this.setState({tests: data})
                    })
                }, 800);
            }
            callback()
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
                    <div key={i}>
                        <br />
                        <b>{object.action}: {object.description}</b> 
                        {object.Status === 'Waiting...' && <p>{object.Status}</p>}
                        {object.Status === 'Success' && <p style={{color:'green'}}>{object.Status}</p>}
                        {object.Status === 'Fail' && <p style={{color:'red'}}>{object.Status}</p>}
                    </div>
                ) 
            })}
        </div>
        );
    }
}

export default Test;