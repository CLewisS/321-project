/* The interface for the service database
 *
 * Functions:
 *   - add: Adds a new entry to the services database.
 *   - get: Gets services that meet the given conditions from the service database. 
 */


var mysql = require("mysql");
var dbConfig = require("./dbConfig");


/* Adds a new entry to the services database
 *
 * Parameters:
 *   - service: The values for the service to be added. 
 *              Must be a JSON object.
 *
 *   - callback: A callback function that is called once the service  has been inserted.
 *               The unique identifier of the inserted service is passed as an argument.
 */

module.exports.add = function (service, callback) {

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return;
    }

    // Get Service Values  
    var values = [service.name, 
                  service.date, 
                  service.dow, 
                  service.time, 
                  service.lat, 
                  service.longi, 
                  service.owner, 
                  service.type, 
                  service.description];
    
    // Insert service into database
    var query = `INSERT INTO services (name, date, dow, time, lat, longi, owner, type, description)
                 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)`;
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
	      console.log(err);
        callback({}, {code: 500, message: err.message});
        return;
      }

      var id = results.insertId;

      var rsvpValues = [id, 0, service.maxCapacity];
      dbConn.query("INSERT INTO rsvp_count VALUES (?, ?, ?)", rsvpValues, (err, results, fields) => {
        if (err) {
	      console.log(err);
          callback({}, {code: 500, message: err.message});
          return;
        }
        callback({id: id});
      });

      // End connection
      dbConn.end(function (err) {
        if (err) {
          callback({}, {code: 500, message: err.message});
          return;
        }
      });

    });

  });

}; 

/* Gets services that meet the given conditions from the service database. 
 *
 * Parameters:
 *   - conditions: The max and min values for the services being retrieved. 
 *                 Must be a JSON object where each key is an attribute, 
 *                 and each value is another JSON object with up to 2 keys (max, min).
 *
 *   - callback: A callback function that is called once the services have been retrieved.
 *               The retrieved services are passed as an argument.
 */
module.exports.get = function(conditions, callback) {

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return;
    }

    var sqlConds = conditions;

    // Build SQL query
    var query = "SELECT * FROM services" 
                + " INNER JOIN rsvp_count ON services.id=rsvp_count.id";
                + " WHERE " + sqlConds.join(" AND ")

    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }
    });

  });

};




/* Delete the service from the service database. 
 *
 * Parameters:
 *   - serviceID: The ID represented the serviced user want to delete.
 *
 *   - callback: A callback function that is called once the services have been deleted.
 *               The retrieved services are passed as an argument.
 */
module.exports.delete = function(serviceID, callback) {

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return;
    }

    
    var query = "DELETE FROM rsvp_count WHERE id = " + serviceID;
  
    // Delete service 
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }
      callback(result);
      // callback({},{});
    });


  });

};


/* Update a service in the services database
 *
 * Parameters:
 *   -serviceID: The ID of the service is gona be updated.
 *   - service: The updated services. 
 *              Must be a JSON object.
 *
 *   - callback: A callback function that is called once the service  has been updated.
 *               The unique identifier of the inserted service is passed as an argument.
 */

module.exports.update = function (serviceID, service, callback) {

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return;
    }

    // Get Service Values  
    var values = [service.name, 
                  service.date, 
                  service.time, 
                  service.lat, 
                  service.longi, 
                  service.owner, 
                  service.type, 
                  service.description];
    
    // Insert service into database
    var query = `UPDATE services SET name = ?, date = ?, time = ?, lat = ?, longi = ?, owner = ?,
    type = ?, description = ? WHERE id = ` + serviceID;
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }

      callback({code:200});
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }

    });

  });

}; 


module.exports.adduserServices = function (service, insertId, callback) {

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({code: 500, message: err.message});
      return;
    }

    var values = [
      service.owner + ":" + insertId.id,
      service.owner,
      "post",
      insertId.id
    ];

    // Insert service into database
    var query = "INSERT INTO userServices (id, username, status, serviceID) VALUES(?, ?, ?, ?)";
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
	      console.log(err);
        callback({code: 500, message: err.message});
        return;
      }
      callback();
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({code: 500, message: err.message});
        return;
      }
    });

  });

}; 


/* Helper function for receive. 
 * It checks if the service is at max capacity.
 *
 * If the service is a max capacity it rejects to a message for the response.
 *
 * If the service isn't at max capacity it increments 
 * the number of RSVPs for the service.
 */
var updateRsvp = function(receiver, serviceID) {
  return new Promise((resolve, reject) => {
    var dbConn = mysql.createConnection(dbConfig.serviceDB);

    // Start database connection  
    dbConn.connect(function (err) {
      if (err) {
        reject({code: 500, message: err.message});
      }

      let query = "SELECT * FROM userServices WHERE id='" + receiver + ":" + serviceID + "'";
      dbConn.query(query, (err, result, fields) => {
        if (err) {
          console.log(err);
          reject({code: 500, message: err.message});
        } else if (result.length != 0) {
		console.log(result);
          reject({code: 200, message: "ALREADY_RSVP"});
	}

        let query = "UPDATE rsvp_count SET numPeople = numPeople + 1 WHERE id=" + serviceID;
        dbConn.query(query, (err, results, fields) => {
          if (err && err.sqlMessage == "Check constraint 'not_over_max' is violated.") {
            reject({code: 200, message: "SERVICE_AT_MAX"});
          } else if (err) {
            reject({code: 500, message: err.message});
          }
    
          resolve(serviceID);
        });


        // End connection
        dbConn.end(function (err) {
          if (err) {
            reject({code: 500, message: err.message});
          }

        });
          
      });

    });

  });

};

module.exports.receive = function (receiver, serviceID, callback) {

  updateRsvp(receiver, serviceID).then((serviceID) => {

    var dbConn = mysql.createConnection(dbConfig.userDB);

    // Start database connection  
    dbConn.connect(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }

      let values = [
        receiver + ":" + serviceID,
        receiver,
        "receive",
        serviceID
      ];

      // Insert service into database
      let query = "INSERT INTO userServices (id, username, status, serviceID) VALUES(?, ?, ?, ?)";
    
      dbConn.query(query, values, (err, results, fields) => {
        if (err) {
          callback({}, {code: 500, message: err.message});
          return;
        }

        callback({id: serviceID});

        // End connection
        dbConn.end(function (err) {
          if (err) {
            callback({}, {code: 500, message: err.message});
            return;
          }
        });
    
      });

    });
  },

  (err) => {
    callback({}, {code: 500, message: err.message});
  });

}; 


module.exports.getReceivedIDs = function(conditions, callback) {

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return;
    }

    // Build SQL query
    var query = "SELECT * FROM userServices WHERE ";

    var sqlConds = conditions;

    query += sqlConds.join(" AND ");
  
    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }
  
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }
    });

  });
};


/* Gets services that meet the given conditions from the service database. 
 *
 * Parameters:
 *   - conditions: id values of the services that should be returned. 
 *                 Must be a JSON object where each key is an attribute, 
 *
 *   - callback: A callback function that is called once the services have been retrieved.
 *               The retrieved services are passed as an argument.
 */
module.exports.getReceivedServices = function(conditions, callback) {

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return;
    }

    // Build SQL query
    var query = "SELECT * FROM services WHERE ";

    var sqlConds = conditions;

    query += sqlConds.join(" OR ");

    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }
  
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }
    });

  });

};
