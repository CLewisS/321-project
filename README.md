# 321-project

## Client
Android studio project

## Server
![](imgs/server_modules.png?raw=true)

### Server Setup
1. Install [Nodejs](https://nodejs.org/en/download/)

2. Clone repo

3. navigate to *server/dbInterface*

4. Run `npm install` in the command line

5. navigate to *server/requestManager*

6. Run `npm install` in the command line

7. Run `node server.js` in the command line

*Optional:*

I have created a very basic web client which can be found in *test.html*. 

If you want to use it for debugging while running the server open [this](http://localhost:3000/)
in your browser.


### Database Setup

1. Follow online [instructions](https://dev.mysql.com/doc/mysql-getting-started/en/#mysql-getting-started-installing)

*Then once MySQL server is running:*

2. `ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';`

3. `CREATE DATABASE services;`

4. 
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
  PRIMARY KEY (id)
);
```

### Database notes

- The detabase configuration must be in the dbConfig.js file in order to connect to it
