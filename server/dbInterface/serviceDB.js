/* The interface for the service database
 */




module.exports.add = function (service) {

  console.log(service);

  var mysql = require('mysql');

  var dbConn = mysql.createConnection({
    host:'localhost',
    user: 'root',
    password: 'password', // Change password to mysql server root user's password
    database: 'services'
  });

  dbConn.connect(function (err) {
    if (err) {
      return console.error('error: ' + err.message);
    }

    console.log('Connected to MySQL server');
  });

};  
