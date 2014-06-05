#!/usr/bin/env node

var request = require('request');

var URL = 'http://localhost:8090/cloud-messaging/';

function send(url, body, method) {
  var uri = URL + url;
    console.log('sending ' + method + ' request to: ' + uri);
    request({
        url: uri,
        method: method || 'GET',
        json: body
    }, function(err, res, body) {
        if (err) {
            console.error("got error response: ", err);
            return;
        }
        console.log("got response status code: " + res.statusCode);
        console.log("got response body: ", body);
    });
}

module.exports = {
    post: function(url, body) {
        send(url, body, 'POST');
    },
    get: function(url, body) {
        send(url, body, 'GET');
    }
};
