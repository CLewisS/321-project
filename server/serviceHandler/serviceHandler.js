/* This module handles service requests
 *
 */

module.exports.getServices = function (req, res) {
  console.log('In service handler: get services');
  res.send('<p> Service Handler: get services </p>');  
}

module.exports.addService = function (req, res) {
/*
  var mysql = require('mysql');

  var dbConn = mysql.createConnection({
    host:'localhost',
    user: 'root',
    password: 'password',
    database: 'services'
  });
  */

  console.log('In service handler: add service');
  res.send('<p> Service Handler: add service </p>');  
};
