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
      if (Object.values(service).length != 8) throw "Service has too few [" + Object.values(service).length + "] values. service: " + JSON.stringify(service);
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
    var query = `INSERT INTO services (name, date, time, lat, longi, owner, type, description)
                 VALUES(?, ?, ?, ?, ?, ?, ?, ?)`;
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log("Inserted");

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
    var query = `SELECT * FROM services WHERE `;

    var sqlConds = conditions;

/*    for (var [key, value] of Object.entries(conditions)) {

      if (value.hasOwnProperty("min")) {
        if (key === "lat" || key === "longi") {
          sqlConds.push(key + " >= " + value["min"]);
        } else {
          sqlConds.push(key + " >=" + "'" + value["min"] + "'");
        }
      }

      if (value.hasOwnProperty("max")) {
        if (key == "lat" || key == "longi") {
          sqlConds.push(key + " <= " + value["max"]);
        } else {
          sqlConds.push(key + " <=" + "'" + value["max"] + "'");
        }
      }
      
      if (key === "type") {
        sqlConds.push("type='" + value + "'");
      }
    }*/

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
