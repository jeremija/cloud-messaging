var req = require('./req.js');

// req.post('push/register', {
//     name: 'user1',
//     device: 'ANDROID',
//     token: 'APA91bFOH2fa1YQiuuBJ1qLgnItFQbAYORX1AA_OUzhgqoyb5Oaz5hMq8gT0kUNi5k8gg56NTh0IoAIhVevcN1-E14i5XsgYJ8tHNnHaw0dy09AnvT5O2C5uQhP0sinkifpXchWTog6p1JOjBq7CGMb9HhqAGBlm53Tls8IhH663KVlne3p4j0k'
// });


req.post('push/sendToUser', {
    usernames: ['user1'],
    data: {
        title: 'bok marko',
        message: 'ovo je poruka'
    }
});