# 321-project

## Client
Android studio project

### Push Notifications
Push notifications are used to send new messages to the recipient.

Handling of the received messages is done in the method *onMessageReceived* in the class **MyFirebaseMessagingService**.

## Server

### Interface

#### User Info
-   User Attributes:
    -   username (string): The User Name of the user. It must be unique.
    -   password (string): The password of the user account.
    -   deviceToken (string): The users current FCM device registration token.
      

#### HTTP Request Methods
-   POST /service: Add a new service. Must contain a JSON object of the service values. The response contains the unique identifier for the service.

    -   Service Attributes:
      -   name (string): The title of the service
      -   date (date): The date of the service. Format: 'YYYY-MM-DD'
      -   dow (string): The day of the week that the service occurs on.
      -   time (time): The time that the service is occuring at. Format: 'hh:mm:ss'
      -   lat (double): The latitudinal coordinate of the event location.
      -   longi (double): The longitudinal coordinate of the event location.
      -   owner (string): The name of the person that created the event.
      -   type (string): The type of service being provided (eg. food, money, etc.)
      -   description (string): A description of the service provided.

-   GET /service: Get services that meet specified conditions. Must contain conditions in query string.
    -   Example: http://xxxxxxx:####/service?date-min=2020-10-15&date-max=2020-11-15&lat-min=49.3456&longi-max=123.456

    `{date: {min: '2020-10-15', max: '2020-11-15'}, lat: {min: 49.3456}, longi: {max: 123.456}}`

-   POST /chat: Add a new message. Must contain a JSON object with users, timestamp, and content.
    -   Message Attributes:
      -   sender (string): The user id of the sender
      -   reciever (string): The user id of the reciever
      -   timestamp (string): The time that the message was sent. Format 'YYYY-MM-DD hh:mm:ss'
      -   content (string): The message content.

-   GET /chat: Get messages.
    -   conditions:
      -   user1 (string): One of the users involved in the message.
      -   user2 (string): The other user involved in the message.
      -   newest (int): The newest message to retrieve from the thread (the first message ever sent between these two users is 1, the second is 2, etc.). 

### Modules
![](imgs/server_modules.png?raw=true)

### Server Setup
1.  Install [Nodejs](https://nodejs.org/en/download/)
 
2.  Clone repo
 
3.  navigate to *server/dbInterface*
 
4.  Run `npm install` in the command line
 
5.  Navigate to *server/chatserver*
 
6.  Run `npm install` in the command line
 
7.  navigate to *server/requestManager*
 
8.  Run `npm install` in the command line
 
9.  Run `node server.js` in the command line
*Optional:*

I have created a very basic web client which can be found in *test.html*. 

If you want to use it for debugging while running the server open [this](http://localhost:3000/)
in your browser.

### Database Setup

1.  Follow online [instructions](https://dev.mysql.com/doc/mysql-getting-started/en/#mysql-getting-started-installing)

2.  navigate to *server/requestManager*

3.  Run `npm install` in the command line

4.  Run `node server.js` in the command line
*Then once MySQL server is running:*

5.  `ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';`

6.  `CREATE DATABASE services;`

7.  `USE services;`

8.   See the code below
```
CREATE TABLE services (
  id INT unsigned NOT NULL AUTO_INCREMENT,
  name VARCHAR(150) NOT NULL,
  date DATE NOT NULL,
  time TIME NOT NULL,
  lat DOUBLE NOT NULL,
  longi DOUBLE NOT NULL,
  owner VARCHAR(150) NOT NULL,
  type VARCHAR(150) NOT NULL,
  description TEXT,
  PRIMARY KEY (id)
);
```

9.  `CREATE DATABASE chat;`
10. `USE chat;`
11. `CREATE TABLE threads (thread VARCHAR(150) NOT NULL, numMess INT unsigned NOT NULL, PRIMARY KEY (thread));`
12. `CREATE TABLE messages (id VARCHAR(160) NOT NULL, time TIMESTAMP NOT NULL, content TEXT NOT NULL, PRIMARY KEY (id));`

### Database notes

-   The database configuration must be in the dbConfig.js file in order to connect to it
