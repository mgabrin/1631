var request = require('request');
const apiUrl = 'http://localhost:3001'

export function startVotingRequest(user, pass){
   return new Promise((resolve, reject) => {
    request.post({
        headers: {
            'Content-Type': 'application/json'
        }, 
        url: 'http://localhost:3001/startVoting',
        form: {
            username: user, 
            password: pass
        }
    }, (err, res, body) => {
        var parsedBody = JSON.parse(body)
        resolve(parsedBody);
    });
   });
}

export function endVotingRequest(user, pass){
    return new Promise((resolve, reject) => {
        request.post({
            headers: {
                'Content-Type': 'application/json'
            }, 
            url: 'http://localhost:3001/endVoting',
            form: {
                username: user, 
                password: pass
            }
        }, (err, res, body) => {
            var parsedBody = JSON.parse(body);
            resolve(parsedBody);
        });
    })
    
}

export function addPosterRequest(user, pass, poster){
    return new Promise((resolve, reject) => {
        request.post({
            headers: {
                'Content-Type': 'application/json'
            }, 
            url: apiUrl + '/addPoster',
            form: {
                username: user, 
                password: pass,
                posterId: poster.posterId,
                category: poster.category,
                creatorYear: poster.creatorYear
            }
        }, (err, res, body) => {
            var parsedBody = JSON.parse(body)
            resolve(parsedBody)
        });
    });
}

export function voteRequest(user, vote){
    return new Promise((resolve, reject) => {
        request.post({
            headers: {
                'Content-Type': 'application/json'
            }, 
            url: apiUrl + '/vote',
            form: {
                username: user, 
                voteName: vote.name
            }
        }, (err, res, body) => {
            var parsedBody = JSON.parse(body)
            resolve(parsedBody);
        });
    })
}

export function getCurrentResultsRequest(){
    return new Promise((resolve, reject) => {
        request.get(apiUrl + '/currentResults', (err, res, body) => {
            var parsedBody = JSON.parse(body);
            resolve(parsedBody)
        });
    });
}