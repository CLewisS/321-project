# 321-project

## Client
Android studio project

## Server
![](imgs/server_modules.png?raw=true)

### Database setup

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
