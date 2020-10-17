/* The interface for the service database
 */


var mysql = require('mysql');
var dbConfig = require('./dbConfig');


/* Adds a new entry to the services database
 *
 * Parameters:
 *   - service: The values for the service to be added. 
 *              Must be a JSON object.
 *
 *   - res: The response object for the http request
 */

module.exports.add = function (service, res) {

  console.log('Adding Service to DB');


  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error('error: ' + err.message);
    }

    console.log('Connected to MySQL server');

    // Get Service Values  
    try {
      var values = Object.values(service);
      if (values.length != 7) throw 'Service has too few [' + values.length + '] values. service: ' + JSON.stringify(service);
    } catch (err) {
      return console.error(err);
    }
    
    // Insert service into database
    var query = `INSERT INTO services (name, date, time, lat, longi, owner, type)
                 VALUES(?, ?, ?, ?, ?, ?, ?)`;
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log('Inserted');
      res.json({id: results.insertId});
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error('error: ' + err.message); 
      }
  
      console.log('Closed connection to MySQL server');
    });

  });

};  
