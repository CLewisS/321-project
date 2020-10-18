/* This module handles service requests
 * 
 * Functions:
 *   - getServices: Gets services requested in the HTTP request, and returns them in the response.
 *   - addService:  Adds the new service from the HTTP request. 
 */


var db = require('../dbInterface/serviceDB.js');

module.exports.getServices = function (req, res) {
  console.log('In service handler: get services');

  // This is just a placeholder for debugging
  // This is also a good example of what the conditions object should be like
  //*****************************************
  var conditions = {
    date:  {min:'2020-10-15', max:'2020-10-25'},
    time:  {max:'15:57:33'},
    lat:   {min: 49.56911},
    longi: {max: 130.456, min: 122.908}
  };
  //*****************************************

  //TODO: Get conditions from request (req) body, and check that it has all the information required
  // The conditions should be in a JSON object (Details in README), and assigned to the variable conditions.
  

  db.get(conditions, (services) => { 
    res.json(services);
  });
}

module.exports.addService = function (req, res) {
  

  console.log('In service handler: add service');
 
  // This is just a placeholder for debugging
  // This is also a good example of what the service object should be like
  //*****************************************
  var service = {
    name: 'A service',
    date: '2020-10-17',
    time: '12:57:33',
    lat:   49.56911,
    longi: 123.456,
    owner: 'Jon',
    type:  'food'
  };
  //*****************************************

  //TODO: Get service from request (req) body, and check that it has all the information required
  // The service should be in a JSON object (Details in README), and assigned to the variable service.

  db.add(service, (id) => {
    res.json(id);
  });

};
