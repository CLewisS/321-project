var mysql = require("mysql");

const serviceDBTest = {host:"ec2-3-13-46-252.us-east-2.compute.amazonaws.com",
                       port: 3306,
                       user: "remote",
                       password: "password",
                       database: "services_test"};


const userDBTest = {host:"ec2-3-13-46-252.us-east-2.compute.amazonaws.com",
                    port: 3306,
                    user: "remote",
                    password: "password",
                    database: "user_test"};


        /* SERIVCE DATABSE */
/************************************/

var initServiceDb = function (callback) {
  var serviceDbConn = mysql.createConnection(serviceDBTest);

  serviceDbConn.connect((err) => {
    if (err) {
      console.log("Couldn't connect to DB: " + err);
      return;
    }
  
    var createServiceTable = `CREATE TABLE IF NOT EXISTS services (
                                id INT unsigned NOT NULL AUTO_INCREMENT,
                                name VARCHAR(150) NOT NULL,
                                date DATE NOT NULL,
  			        dow VARCHAR(15) NOT NULL,
                                time TIME NOT NULL,
                                lat DOUBLE NOT NULL,
                                longi DOUBLE NOT NULL,
                                owner VARCHAR(150) NOT NULL,
                                type VARCHAR(150) NOT NULL,
                                description TEXT,
                                PRIMARY KEY (id)
                              );`

  
    serviceDbConn.query(createServiceTable, (err) => {
      if (err) {
        console.log(err);
        return;
      }

      var service1 = ["food service", "2020-8-17", "Monday", "12:57:33", 49.56911, 123.456, "Caleb", "food", "This is a description"];
      var query1 = `INSERT INTO services (name, date, dow, time, lat, longi, owner, type, description)
                    VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)`;

      serviceDbConn.query(query1, service1, (err) => {
        if (err) {
          console.log(err);
          return;
	}

        var service2 = ["A service", "2020-11-17", "Friday", "12:57:33", 49.56911, 123.456, "Caleb", "food", "This is a description"];
        var query2 = `INSERT INTO services (name, date, dow, time, lat, longi, owner, type, description)
                      VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)`;
  
        serviceDbConn.query(query2, service2, (err) => {
          if (err) {
            console.log(err);
            return;
  	  }

          serviceDbConn.end((err) => {
            if (err) {
              console.log(err);
              return;
            }
            callback();
          });
  
        });

      });
  
    });
  
  });
};

module.exports.initServiceDb = initServiceDb;

var tearDownServiceDb = function (callback) {
  var serviceDbConn = mysql.createConnection(serviceDBTest);

  serviceDbConn.connect((err) => {
    if (err) {
      console.log("Couldn't connect to DB");
      return;
    }
  
    var dropServiceTable = "DROP TABLE IF EXISTS services";
  
    serviceDbConn.query(dropServiceTable, (err) => {
      if (err) {
        console.log(err);
        return;
      }
  
      serviceDbConn.end((err) => {
        if (err) {
          console.log(err);
          return;
        }
	callback();
      });
  
    });
  
  });

};

module.exports.tearDownServiceDb = tearDownServiceDb;


        /* USER DATABSE */
/************************************/
var initUserDb = function (callback) {
  var userDbConn = mysql.createConnection(userDBTest);

  userDbConn.connect((err) => {
    if (err) {
      console.log("Couldn't connect to DB");
      return;
    }

    var createuserTable = `CREATE TABLE IF NOT EXISTS users (
                             username VARCHAR(150) NOT NULL,
                             deviceToken VARCHAR(150),
                             password VARCHAR(150) NOT NULL,
                             PRIMARY KEY (username)
                           );`
  
    userDbConn.query(createuserTable, (err) => {
      if (err) {
        console.log(err);
        return;
      } 

      userDbConn.query("DELETE FROM users", (err) => {
        if (err) {
          console.log(err);
          return;
        }

        userDbConn.query("INSERT INTO users (username, password, deviceToken) VALUES ('Caleb', 'pass', 'AFWEf7823rtubSDV_sA97GBUahaeibreagfaergi')", 
          (err) => {
            if (err) {
              console.log(err);
              return;
            }
            var createUserServicesTable = `CREATE TABLE IF NOT EXISTS userServices (
                                             id INT unsigned NOT NULL AUTO_INCREMENT, 
                                             username VARCHAR(50) NOT NULL,
                                             status VARCHAR(15) NOT NULL,
                                             serviceID INT unsigned NOT NULL,
                                             PRIMARY KEY (id),
                                             FOREIGN KEY (username) REFERENCES users(username)
                                           )`;
            userDbConn.query(createUserServicesTable, (err) => {
              if (err) {
                console.log(err);
                return;
              }

              userDbConn.end((err) => {
                if (err) {
                  console.log(err);
                  return;
                }
                callback();
              });

            });
        });

      });
        
    });

  });
};

module.exports.initUserDb = initUserDb;

var tearDownUserDb = function (callback) {
  var userDbConn = mysql.createConnection(userDBTest);

  userDbConn.connect((err) => {
    if (err) {
      console.log("Couldn't connect to DB");
      return;
    }
  
    userDbConn.query("DROP TABLE IF EXISTS userServices", (err) => {
      if (err) {
        console.log(err);
        return;
      } 

      userDbConn.query("DROP TABLE IF EXISTS users", (err) => {
        if (err) {
          console.log(err);
          return;
        }

        userDbConn.end((err) => {
          if (err) {
            console.log(err);
            return;
          }
          callback();
        });

      });
  
    });
  
  });

};

module.exports.tearDownUserDb = tearDownUserDb;
