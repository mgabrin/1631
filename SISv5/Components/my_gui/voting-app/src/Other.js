import React, { Component } from 'react';
import './App.css';
import { getCurrentResultsRequest } from './Utilities';

class Other extends Component {
    componentWillMount(){
        getCurrentResultsRequest()
        .then(parsedBody => {
            this.setState({candidates: parsedBody.Candidates})
        })

        getTotalResultsRequest()
        .then(parsedBody => {
            seventeenData = _.filter(parsedData, (entry) => {
                entry.year === '2017'
            });
            sixteenData = _.filter(parsedData, (entry) => {
                entry.year === '2016'
            });
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
                             return <tr>
                                <td>{object.name}</td>
                                <td>{object.total}</td>
                                <td>{object.total}</td>
                            </tr> 
                        })}
                    </tbody>               
                </table>
            </center>

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
                        <tr>
                            <td>1</td>
                            <td>2</td>
                            <td>3</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr> 
                    </tbody>               
                </table>
            </center>
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