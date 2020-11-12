var mysql = require("mysql");

const serviceDBTest = {host: "localhost",
                       user: "root",
                       password: "password",
                       database: "services_test"};


const userDBTest = {host: "localhost",
                    user: "root",
                    password: "password",
                    database: "user_test"};


        /* SERIVCE DATABSE */
/************************************/

var initServiceDb = function () {
  var serviceDbConn = mysql.createConnection(serviceDBTest);

  serviceDbConn.connect((err) => {
    if (err) {
      consople.log("Couldn't connect to DB");
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
      } else {
        var createUserServicesTable = `CREATE TABLE IF NOT EXISTS userServices (
                                         id INT unsigned NOT NULL AUTO_INCREMENT, 
  	                                 username VARCHAR(50) NOT NULL,
                                         status VARCHAR(15) NOT NULL,
                                         serviceID INT unsigned NOT NULL,
                                         PRIMARY KEY (id)
                                       )`;
        serviceDbConn.query(createUserServicesTable, (err) => {
          if (err) {
            console.log(err);
  	  }
        });
      }
  
      serviceDbConn.end((err) => {
        if (err) {
          console.log(err);
        }
      });
  
    });
  
  });
};

module.exports.initServiceDb = initServiceDb;

var tearDownServiceDb = function () {
  var serviceDbConn = mysql.createConnection(serviceDBTest);

  serviceDbConn.connect((err) => {
    if (err) {
      console.log("Couldn't connect to DB");
      return;
    }
  
    var dropServiceTable = "DROP TABLE services";
  
    serviceDbConn.query(dropServiceTable, (err) => {
      if (err) {
        console.log(err);
      } else {
        var dropUserServicesTable = "DROP TABLE userServices";
        serviceDbConn.query(dropUserServicesTable, (err) => {
          if (err) {
            console.log(err);
  	  }
        });
      }
  
      serviceDbConn.end((err) => {
        if (err) {
          console.log(err);
        }
      });
  
    });
  
  });

};

module.exports.tearDownServiceDb = tearDownServiceDb;


        /* USER DATABSE */
/************************************/
var initUserDb = function () {
  var userDbConn = mysql.createConnection(userDBTest);

  userDbConn.connect((err) => {
    if (err) {
      consople.log("Couldn't connect to DB");
      return;
    }
  
    var createuserTable = `CREATE TABLE IF NOT EXISTS user (
                             username VARCHAR(150) NOT NULL,
                             deviceToken VARCHAR(150) NOT NULL,
                             password VARCHAR(150) NOT NULL,
                             PRIMARY KEY (username)
                           );`
  
    userDbConn.query(createuserTable, (err) => {
      if (err) {
        console.log(err);
      } else {
        userDbConn.query("INSERT INTO user (username, deviceToken, password) VALUES ('Caleb', 'pass', 'AFWEf7823rtubSDV_sA97GBUahaeibreagfaergi')", 
          (err) => {
            if (err) {
              console.log(err);
            } else {

	    }
	  });

        var createUserServicesTable = `CREATE TABLE IF NOT EXISTS userServices (
                                         id INT unsigned NOT NULL AUTO_INCREMENT, 
                                         username VARCHAR(50) NOT NULL,
                                         status VARCHAR(15) NOT NULL,
                                         serviceID INT unsigned NOT NULL,
                                         PRIMARY KEY (id),
                                         FOREIGN KEY (username) REFERENCES user(username)
                                       )`;
        userDbConn.query(createUserServicesTable, (err) => {
          if (err) {
            console.log(err);
          }
        });
      }
  
      userDbConn.end((err) => {
        if (err) {
          console.log(err);
        }
      });
  
    });
  
  });
};

module.exports.initUserDb = initUserDb;

var tearDownUserDb = function () {
  var userDbConn = mysql.createConnection(userDBTest);

  userDbConn.connect((err) => {
    if (err) {
      console.log("Couldn't connect to DB");
      return;
    }
  
    userDbConn.query("DROP TABLE userServices", (err) => {
      if (err) {
        console.log(err);
      } else {

        userDbConn.query("DROP TABLE user", (err) => {
          if (err) {
            console.log(err);
  	  }
        });

      }
  
      userDbConn.end((err) => {
        if (err) {
          console.log(err);
        }
      });
  
    });
  
  });

};

module.exports.tearDownUserDb = tearDownUserDb;
