var req = require('./req.js');

req.post('push/unregister', {
    name: 'user1',
    device: 'ANDROID',
    token: 'token1'
});
