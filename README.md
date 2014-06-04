# cloud-messaging

Java REST API for Google Cloud Messaging and Apple Push Notification services. Uses MongoDB for persistent storage of device tokens.

  

*Note: Apple Push Notifications service support not yet implemented*

## configuration
Create a file `/src/main/resources/cloud.properties`. It should contain the following content:

```
gcm.api.key = *your GCM api key*
```

## usage
After you have compiled the Maven project and created the `cloud.properties` config file, you should be able to publish the application to Tomcat 7.

The following REST API is exposed:

### POST /push/register

Stores the user info in the database. Multiple users with the same name are allowed, as user can have more than one device.

Request body should contain the User object in JSON format:

```
{
  name: 'user-name',
  device: ANDROID,
  token: 'device-token
}
```

See `com.steinerize.cloud.messaging.domain.User` for more details.

### POST /push/unregister

Removes the user info from the database. 

If the `token` field is defined, only the entry for the specific token will be deleted. If only a `name` property is defined, all tokens for specific user will be deleted.

Request body should contain the User object in JSON format:

```
{
  name: 'user-name',
  device: ANDROID,
  token: 'device-token
}
```

See `com.steinerize.cloud.messaging.domain.User` class for more details.

### POST /push/send

Sends a message to specific devices (as defined in PushMessage#androidToken and PushMessage#appleToken).

Request body should contain the PushMessage object in JSON format:

```
{
  data: {
    title: 'notification title',
    message: 'notification message'
  },
  androidTokens: ['token1', 'token2', 'token3'],
  iosTokens: ['token4', 'token5', 'token6']
}
```

See `com.steinerize.cloud.messaging.domain.PushMessage` class for more details.

### POST /push/sendToUser

Sends a message to all users' devices (as defined in UserMessage#usernames).

Request body sohuld contain the UserMessage object in JSON format:

```
{
  data: {
    title: 'notification title',
    message: 'notification message'
  },
  usernames: ['user1', 'user2']
}
```

See `com.steinerize.cloud.messaging.domain.UserMessage` class.
