import React, { Component } from 'react';
import './App.css';
import { getCurrentResultsRequest, getTotalResultsRequest } from './Utilities';
var _ = require('lodash');
var request = require('request');

const apiUrl = 'http://localhost:3001'

class Other extends Component {
    componentWillMount(){
        request.get(apiUrl + '/votingStatus', (err, res, body) =>{
            var parsedBody = JSON.parse(body);
            this.setState({votingStatus: parsedBody.Status})
        })

        getCurrentResultsRequest()
        .then(parsedBody => {
            this.setState({candidates: parsedBody.Candidates})
        })

        getTotalResultsRequest()
        .then((parsedBody) => {
            console.log('here');
            console.log(parsedBody)
            var seventeenData = _.filter(parsedBody.data, (entry) => {
                if(entry){
                    console.log(entry.year)
                    return entry.year === '2017'
                }
            });
            var seventeenSum = _.chain(seventeenData)
                            .map(entry => { 
                                return parseInt(entry.total); 
                            })
                            .sum()
                            .value();
            console.log(seventeenSum);
            console.log('Seventeen data');
            console.log(seventeenData);

            _.forEach(seventeenData, entry => {
                entry['percentage'] = ((entry.total / seventeenSum) * 100).toFixed(2);
            });

            var sixteenData = _.filter(parsedBody.data, (entry) => {
                if(entry){
                    return entry.year === '2016'
                }
            });

            

            var sixteenSum = _.chain(sixteenData)
                            .map(entry => { return parseInt(entry.total); })
                            .sum()
                            .value();

            _.forEach(sixteenData, entry => {
                entry['percentage'] = ((entry.total / sixteenSum) * 100).toFixed(2);
            });
            console.log(sixteenSum)
            console.log('sixteenData');
            console.log(sixteenData)
            var totalData = {
                'sixteenData': sixteenData,
                'seventeenData': seventeenData
            }
            this.setState({totalData: totalData});
        });
    }


    state = {
        votingStatus: false,
        candidates: [
          {'name': 'hello', 'total': 0}, 
          {'name':'world', 'total':0}
        ],
        totalData: {
            sixteenData: [],
            seventeenData: []
        }
    }

    render() {
        return (
        <div className="App">
            <h1>Vote</h1>

            {this.state.candidates.map(function(object, i){
                return <p key={i}><b>{object.name}</b> : {object.total}</p> 
            })}
            <hr width="50%" />
            {this.state.totalData.seventeenData.length > 0 &&
                <div>
                <h3><u>2017 data</u></h3>
                <center>
                    <table>
                        <thead>
                            <tr>
                                <th>Category</th>
                                <th># Votes</th>
                                <th>Percentage</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.totalData.seventeenData.map(function(object, i){
                                return <tr key={i}>
                                    <td>{object.name}</td>
                                    <td>{object.total}</td>
                                    <td>{object.percentage}%</td>
                                </tr> 
                            })}
                        </tbody>               
                    </table>
                </center>
                </div>
            }
            {this.state.totalData.sixteenData.length !== 0 && 
                <div>
                <h3><u>2016 data</u></h3>
                <center>
                    <table>
                        <thead>
                            <tr>
                                <th>Category</th>
                                <th># Votes</th>
                                <th>Percentage</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.totalData.sixteenData.map(function(object, i){
                                return <tr key={i}>
                                    <td>{object.name}</td>
                                    <td>{object.total}</td>
                                    <td>{object.percentage}%</td>
                                </tr> 
                            })}
                        </tbody>               
                    </table>
                </center>
                </div>
            }
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

export default Other;