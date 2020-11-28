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
      return;
    }
  
    var query = `CREATE TABLE IF NOT EXISTS services (
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

  
    serviceDbConn.query(query, (err) => {
      if (err) {
        return;
      }

      var service = ["food service", "2020-8-17", "Monday", "12:57:33", 49.56911, 123.456, "Caleb", "food", "This is a description"];
      var query = `INSERT INTO services (name, date, dow, time, lat, longi, owner, type, description)
                    VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)`;

      serviceDbConn.query(query, service, (err) => {
        if (err) {
          return;
	}

        var service = ["A service", "2020-11-17", "Friday", "12:57:33", 49.56911, 123.456, "Caleb", "food", "This is a description"];
        var query = `INSERT INTO services (name, date, dow, time, lat, longi, owner, type, description)
                      VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)`;
  
        serviceDbConn.query(query, service, (err) => {
          if (err) {
            return;
  	  }

          var query = `CREATE TABLE IF NOT EXISTS rsvp_count( 
	                 id INT UNSIGNED NOT NULL,
			 numPeople INT NOT NULL,
			 maxCapacity INT NOT NULL,
			 PRIMARY KEY (id),
			 FOREIGN KEY (id) REFERENCES services (id),
			 CHECK (numPeople <= maxCapacity)
		       )`;

          serviceDbConn.query(query, (err) => {
            if (err) {
              return;
            }

            var query = "INSERT INTO rsvp_count VALUES (1, 0, 5)";

            serviceDbConn.query(query, (err) => {
              if (err) {
                return;
              }

	    });

            query = "INSERT INTO rsvp_count VALUES (2, 0, 7)";

            serviceDbConn.query(query, (err) => {
              if (err) {
                return;
              }
            });

            serviceDbConn.end((err) => {
              if (err) {
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

module.exports.initServiceDb = initServiceDb;

var tearDownServiceDb = function (callback) {
  var serviceDbConn = mysql.createConnection(serviceDBTest);

  serviceDbConn.connect((err) => {
    if (err) {
      return;
    }
  
    var dropRsvpTable = "DROP TABLE IF EXISTS rsvp_count";
  
    serviceDbConn.query(dropRsvpTable, (err) => {
      if (err) {
        return;
      }

      var dropServiceTable = "DROP TABLE IF EXISTS services";
  
      serviceDbConn.query(dropServiceTable, (err) => {
        if (err) {
          return;
        }
  
        serviceDbConn.end((err) => {
          if (err) {
            return;
          }
          callback();
        });
  
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
        return;
      } 

      userDbConn.query("DELETE FROM users", (err) => {
        if (err) {
          return;
        }

        userDbConn.query("INSERT INTO users (username, password, deviceToken) VALUES ('Caleb', 'pass', 'AFWEf7823rtubSDV_sA97GBUahaeibreagfaergi')", 
          (err) => {
            if (err) {
              return;
            }
            var createUserServicesTable = `CREATE TABLE IF NOT EXISTS userServices (
                                             id VARCHAR(55), 
                                             username VARCHAR(50) NOT NULL,
                                             status VARCHAR(15) NOT NULL,
                                             serviceID INT unsigned NOT NULL,
                                             PRIMARY KEY (id),
                                             FOREIGN KEY (username) REFERENCES users(username)
                                           )`;
            userDbConn.query(createUserServicesTable, (err) => {
              if (err) {
                return;
              }

              userDbConn.end((err) => {
                if (err) {
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
      return;
    }
  
    userDbConn.query("DROP TABLE IF EXISTS userServices", (err) => {
      if (err) {
        return;
      } 

      userDbConn.query("DROP TABLE IF EXISTS users", (err) => {
        if (err) {
          return;
        }

        userDbConn.end((err) => {
          if (err) {
            return;
          }
          callback();
        });

      });
  
    });
  
  });

};

module.exports.tearDownUserDb = tearDownUserDb;
