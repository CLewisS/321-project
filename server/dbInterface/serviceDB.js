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

  
  console.log("Adding Service to DB");


  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Get Service Values  
    try {
      if (Object.values(service).length !== 9){ 
        throw "Service has too few [" + Object.values(service).length + "] values. service: " + JSON.stringify(service);
    }
  } catch (err) {
      return console.error(err);
    }
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
        return console.error(err.message);
      }
  
      console.log("Inserted");
      console.log(results);
      callback({id: results.insertId});
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }

      console.log("Closed connection to MySQL server");
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
  console.log("Getting Services from DB");

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Build SQL query
    var query = "SELECT * FROM services WHERE ";

    var sqlConds = conditions;

    query += sqlConds.join(" AND ");
  
    console.log(query);

    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
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
  console.log("Getting Services from DB");

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Build SQL query
    var query = "DELETE FROM services WHERE id = " + serviceID;
  
    console.log(query);

    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
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

  console.log("Adding Service to DB");


  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Get Service Values  
    try {
      if (Object.values(service).length !== 8){
         throw "Service has too few [" + Object.values(service).length + "] values. service: " + JSON.stringify(service);
      }
        } catch (err) {
      return console.error(err);
    }
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
        return console.error(err.message);
      }
  
      console.log("Updated");

      callback(results);
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
    });

  });

}; 


module.exports.adduserServices = function (service, insertId) {

  console.log("Adding Service to userDB userServices table.");


  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    var values = [
      service.owner,
      "post",
      insertId.id
    ];
    console.log(insertId.id);
    // Insert service into database
    var query = "INSERT INTO userServices (username, status, serviceID) VALUES(?, ?, ?)";
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log("Inserted");

     
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
    });

  });

}; 


module.exports.receive = function (receiver,serviceID, callback) {

  console.log("Adding Service to userDB userServices table.");


  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    var values = [
      receiver,
      "receive",
      serviceID
    ];

    console.log(values);
    
    // Insert service into database
    var query = "INSERT INTO userServices (username, status, serviceID) VALUES(?, ?, ?)";
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log("Inserted");

      callback(results);
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
    });

  });

}; 


module.exports.getReceivedIDs = function(conditions, callback) {

  console.log("Getting received services from DB");

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Build SQL query
    var query = "SELECT * FROM userServices WHERE ";

    var sqlConds = conditions;

    query += sqlConds.join(" AND ");
  
    console.log(query);

    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
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
  console.log("Getting Services from DB");

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Build SQL query
    var query = "SELECT * FROM services WHERE ";

    var sqlConds = conditions;

    query += sqlConds.join(" OR ");
  
    console.log(query);

    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
    });

  });

};




