var express = require('express');
var net = require('net-socket');
var bodyParser = require('body-parser');

const PASSCODE = 'm1k3+b3n';
var votingStatus = false;

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

app.get('/candidates', (req, res) => {
    res.json({
        Candidates: ['Test 1', 'Test 2', 'Test 3', 'Test 4']
    });
})

app.get('/currentResults', (req, res) => {
    res.json({
        Candidates: [
            {
                'name': 'Test 1',
                'total': 5
            },
            {
                'name': 'Test 2',
                'total': 3
            },
            {
                'name': 'Test 3',
                'total': 0
            },
            {
                'name': 'Test 4',
                'total': 1
            },
        ]
    })
});

app.post('/startVoting', (req, res) => {
    if (req.body.password !== PASSCODE) {
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
    if (req.body.password !== PASSCODE) {
        res.json({
            'Success':'False',
            'Message':'Invalid Passcode'
        });
    } else {
        votingStatus = false;
        res.json({
            Success: 'True',
            Status: votingStatus
        });
    }
});

app.post('/addPoster', (req, res) => {

});

app.post('/vote', (req, res) => {
    res.json({
        'Success': 'False',
        'Message': 'User has already voted'
    })
});


app.post('/getVotingResults', (req, res) => {

});

/* istanbul ignore next */
if (!module.parent) {
    var socket = net.connect(53217, '127.0.0.1');
    socket.setEncoding('utf8');
    socket.on('connect', () => {
        console.log('connected');
        socket.write('(Scope$$$SIS.Scope1$$$\
        MessageType$$$Connect$$$Role$$$Basic$$$Name$$$GUI$$$)');
    });
  app.listen(3001);
  console.log('Express started on port 3001');
}