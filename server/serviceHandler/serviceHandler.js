/* This module handles service requests
 *
 */

var db = require('../dbInterface/serviceDB.js');

module.exports.getServices = function (req, res) {
  console.log('In service handler: get services');
  res.send('<p> Service Handler: get services </p>');  
}

module.exports.addService = function (req, res) {
  

  console.log('In service handler: add service');
 
  // This is just a placeholder for debugging
  var service = {
    name: 'A service',
    date: '2020-10-17',
    time: '12:57:33',
    lat:   49.56911,
    longi: 123.456,
    owner: 'Bood',
    type:  'food'
  };

  db.add(service, res);

};
