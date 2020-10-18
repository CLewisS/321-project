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

module.exports.add = function (service, callback) {

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

      callback({id: results.insertId});
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


module.exports.get = function(conditions, callback) {
  console.log('Getting Services from DB');

  var dbConn = mysql.createConnection(dbConfig.serviceDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error('error: ' + err.message);
    }

    console.log('Connected to MySQL server');

    // Build SQL query
    var query = `SELECT * FROM services WHERE `;

    var sqlConds = [];

    for (var [key, value] of Object.entries(conditions)) {

      if (value.hasOwnProperty('min')) {
        if (key == 'lat' || key == 'longi') {
          sqlConds.push(key + ' >= ' + value['min']);
        } else {
          sqlConds.push(key + ' >=' + '\'' + value['min'] + '\'');
        }
      }

      if (value.hasOwnProperty('max')) {
        if (key == 'lat' || key == 'longi') {
          sqlConds.push(key + ' <= ' + value['max']);
        } else {
          sqlConds.push(key + ' <=' + '\'' + value['max'] + '\'');
        }
      }

    }

    query += sqlConds.join(' AND ');
  
    //query = 'SELECT * FROM services WHERE date >= \'2020-10-15\' AND date <= \'2020-10-25\' AND time <= \'15:57:3\' AND lat >= 49.56911 AND longi >= 122.908 AND longi <= 130.456';

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
        return console.error('error: ' + err.message); 
      }
  
      console.log('Closed connection to MySQL server');
    });

  });

};
