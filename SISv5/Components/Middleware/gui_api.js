var express = require('express');
var net = require('net-socket');
var bodyParser = require('body-parser');

const PASSCODE = 'm1k3+b3n';
const USERNAME = 'admin';

var votingStatus = false;
var addedPoster = false;
var finalData = null;
var finalTotals = null;


var _ = require('lodash');
var app = express();

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json())

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", 
  "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

app.get('/votingStatus', (req, res) => [
    res.json({
        Status: votingStatus
    })
]);

app.get('/currentResults', (req, res) => {
    if(addedPoster){
        if(!votingStatus){
            res.json({
                Candidates: finalTotals
            })
        } else {
            console.log('added is good')
            socket.write('(Scope$$$SIS.Scope1$$$\
                MessageType$$$Alert$$$\
                Sender$$$GUI$$$\
                Receiver$$$InterfaceServer$$$\
                MessageCode$$$701$$$\
                From$$$mike$$$\
                Subject$$$Request Report$$$\
                Body$$$Passcode:m1k3+b3n$$$)\n');
            socket.on('data', (data) => {
                try{
                    var entries = data.split('$$$');
                    var confirmIndex = _.findIndex(entries, (entry) => { return entry === '3'})
                    var serverIndex = _.findIndex(entries, (entry) => { return entry === 'InterfaceServer'})
                    var statusIndex = _.findIndex(entries, (entry) => { return entry === 'Status'})
                    var valuesIndex = _.findIndex(entries, (entry) => { return entry === 'Values'})
                    if(confirmIndex !== -1 && serverIndex !== -1 && statusIndex !== -1 && valuesIndex != -1){
                        var posterInfo = entries[valuesIndex + 1]
                        var candidates = posterInfo.split(';')
                        var returnCandidates = []
                        _.forEach(candidates, (candidate) => {
                            var elements = candidate.split(',');
                            returnCandidates.push({
                                'name': elements[0],
                                'total': elements[1]
                            })
                        });
                        finalTotals = returnCandidates
                        res.json({
                            Candidates: returnCandidates
                        });
                    }
                } catch(e){
                    console.log(e);
                }
            });
        }
    } else {
        res.json({
            Candidates: []
        })
    }
});

app.get('/getFullData', (req, res) => {
    console.log('IN GET FULL DATA');
    res.json({
        Success: 'True',
        Status: votingStatus,
        data: finalData
    })
});

app.post('/startVoting', (req, res) => {
    if (req.body.password !== PASSCODE || req.body.username !== USERNAME ) {
        res.json({
            'Success':'False',
            'Message':'Invalid Passcode'
        });
    } else {
        votingStatus = true;
        res.json({
            Success: 'True',
            Status: votingStatus
        });
    }
    
});

app.post('/endVoting', (req, res) => {
    if (req.body.password !== PASSCODE || req.body.username !== USERNAME || !votingStatus) {
        res.json({
            'Success':'False',
            'Message':'Invalid Credentials'
        });
    } else {
        console.log('in here');
        var returnObj;
        votingStatus = false;
        socket.write('(Scope$$$SIS.Scope1$$$\
        MessageType$$$Alert$$$\
        Sender$$$GUI$$$\
        Receiver$$$InterfaceServer$$$\
        MessageCode$$$701$$$\
        From$$$mike$$$\
        Subject$$$End Voting$$$\
        Body$$$Passcode:' + PASSCODE + '$$$)\n');
        socket.on('data', (data) => {
            try{
                console.log('End voting read');
                console.log(data)
                var entries = data.split('$$$');
                var confirmIndex = _.findIndex(entries, (entry) => { return entry === '3'})
                var serverIndex = _.findIndex(entries, (entry) => { return entry === 'PosterComponent'})
                var statusIndex = _.findIndex(entries, (entry) => { return entry === 'Status'})
                var categoriesIndex = _.findIndex(entries, (entry) => { return entry === 'Categories'})
                if(confirmIndex !== -1 && serverIndex != -1 && categoriesIndex != -1){
                    var posterInfo = entries[categoriesIndex + 1]
                    var candidates = posterInfo.split(';')
                    var returnCandidates = []
                    _.forEach(candidates, (candidate) => {
                        var elements = candidate.split(',');
                        returnCandidates.push({
                            'name': elements[0],
                            'total': elements[1],
                            'year': elements[2] 
                        })
                    });
                    finalData = returnCandidates
                    res.json({
                        Success: 'True',
                        Status: votingStatus,
                        data: returnCandidates
                    });    
                } else if(serverIndex != -1){
                    res.json({
                        Success: 'False', 
                        Status: votingStatus
                    })
                }
            } catch(e){
                console.log(e);
            }
            
        });
    }
});

app.post('/addPoster', (req, res) => {
    if (req.body.password !== PASSCODE || 
    req.body.username !== USERNAME|| 
    !req.body.posterId || 
    !req.body.category || 
    !req.body.creatorYear) {
        console.log('in here');
        res.json({
            'Success':'False',
            'Message':'Invalid Entry'
        });
    } else {
        addedPoster = true;
        var posterName = req.body.posterId
        var returnObj;
        socket.write('(Scope$$$SIS.Scope1$$$\
        MessageType$$$Alert$$$\
        Sender$$$GUI$$$\
        Receiver$$$InterfaceServer$$$\
        MessageCode$$$701$$$\
        From$$$mike$$$\
        Subject$$$Initialize Tally Table$$$\
        Id$$$' + req.body.posterId + '$$$\
        Year$$$' + req.body.creatorYear + '$$$\
        Category$$$' + req.body.category + ')\n');
        socket.on('data', (data) => {
            console.log(data);
            try{
                var entries = data.split('$$$');
                var confirmIndex = _.findIndex(entries, (entry) => { return entry === '3'})
                var serverIndex = _.findIndex(entries, (entry) => { return entry === 'InterfaceServer'})
                var statusIndex = _.findIndex(entries, (entry) => { return entry === 'Status'})
                if(confirmIndex !== -1 && serverIndex != -1){
                    res.json({
                        Success: 'True',
                        Status: votingStatus
                    });    
                }
            } catch(e){
                console.log(e);
            }
            
        });
    }
});

app.post('/vote', (req, res) => {
    console.log(req.body.username)
    socket.write('(Scope$$$SIS.Scope1$$$\
        MessageType$$$Alert$$$\
        Sender$$$GUI$$$\
        Receiver$$$InterfaceServer$$$\
        MessageCode$$$701$$$\
        From$$$' + req.body.username + '$$$\
        Subject$$$cs1631 vote$$$\
        Body$$$' + req.body.voteName + '$$$)\n');

    socket.on('data', (data) => {
        console.log('voted')
        console.log(data)
        var entries = data.split('$$$');
        //If the vote was a success, there will be a 3 in the return data
        var successIndex = _.findIndex(entries, (entry) => { return entry === '3'})
        //If the selected poster was not valid, then there will be a 2 in the
        //Returned data
        var invalidPosterIndex = _.findIndex(entries, (entry) => { return entry === '2'})
        //If the voter already voted, then there will be a 1 in the returned data
        var invalidVoterIndex = _.findIndex(entries, (entry) => { return entry === '1'})
        
        var returnCodeIndex = _.findIndex(entries, (entry) => {return entry === '711'})
        try{
        if(successIndex !== -1 && returnCodeIndex !== -1){
            res.json({
                'Success': 'True',
                'Message': 'Success'
            })
        } else if(returnCodeIndex !== -1){
            if(invalidPosterIndex != -1){
                res.json({
                    'Success': 'False',
                    'Message': 'Invalid Poster'
                });
            } else if(invalidVoterIndex != -1){
                res.json({
                    'Success': 'False',
                    'Message': 'User has already voted'
                });
            }
        }
        } catch(e){
            console.log(e)
        }
    })
    
});


/* istanbul ignore next */
if (!module.parent) {
    var socket = net.connect(53217, '127.0.0.1');
    socket.setEncoding('utf8');
    socket.on('connect', () => {
        console.log('connected');
        socket.write('(Scope$$$SIS.Scope1$$$\
        MessageType$$$Connect$$$Role$$$Basic$$$Name$$$GUI$$$)\n');
    });
  app.listen(3001);
  console.log('Express started on port 3001');
}