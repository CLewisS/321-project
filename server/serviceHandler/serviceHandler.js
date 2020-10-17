/* This module handles service requests
 *
 */

module.exports.getServices = function (req, res) {
  console.log('In service handler: get services');
  res.send('<p> Service Handler: get services </p>');  
}

module.exports.addService = function (req, res) {
  
  var db = require('../dbInterface/serviceDB.js');

  console.log('In service handler: add service');
  
  db.add('Hi');

  res.send('<p> Service Handler: add service </p>');  
};
