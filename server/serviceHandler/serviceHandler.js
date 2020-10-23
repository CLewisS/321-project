/* This module handles service requests
 * 
 * Functions:
 *   - getServices: Gets services requested in the HTTP request, and returns them in the response.
 *   - addService:  Adds the new service from the HTTP request. 
 */

const { log, Console } = require("console");
const { S_IFDIR } = require("constants");


//var db = require("../dbInterface/serviceDB.js");
var Create_serviceAttributes = ["name","date", "dow", "time", "lat", "longi", "type", "owner", "description"];
var Search_serviceAttributes = ["date-min","date-max", "lat-min","lat-max","longi-min","longi-max", "type"];


/* Check if this key is valid during the get service process.
 * Parameters:
 *   - key: one of the key that is read from the query. 
 */
var isValidKey_for_search = function (key) {
  if(Search_serviceAttributes.includes(key)){
    return true
  }
  return false;
}; 




/* For every key, create a string for eadch key of the query
 * Parameters:
 *   - key: one of the key that is read from the query. 
 *   - condition_json: the json abject was read form the url query string.
 */
var create_single_attribute_string = function(key, condition_json){
     var str = "";
     var key0 = key.split("-")[0];
     switch(key){
      case "type": case "name": case "dow": case "owner": case "description":
         str = key +  " = '" + condition_json[key] + "'" ;
         break;
      case "longi": case "lat": 
         str = key + " = " + condition_json[key];
         break;
      case "date": 
         str = key + " = '" + condition_json[key] + "'";
         break; 
      case "date-min":
         str = key0 +  " >= '" + condition_json[key] + "'" ;
         break;
      case "lat-min": case "longi-min":
         str = key0 +  " >= " + condition_json[key];
         break;
      case "date-max":
         str = key0 +  " <= '" + condition_json[key] + "'" ;
         break;
      case "lat-max": case "longi-max":
         str = key0 +  " <= " + condition_json[key];
         break;
      default:
        console.log("There are something wrong, " +  key + " is not one of the valid key!!");
     }
     return str;
};


/* Create an array of string, each string was consist of one key and its crossponding value (ex. "date >= 2020-10-16")
 * Parameters:
 *    - condition_json: the json abject was read form the url query string.
 */
var create_Condition_array = (condition_json)=>{
    const keys = Object.keys(condition_json);
    let conditions = [];
    for (var key of keys) {
      if (isValidKey_for_search(key)){
        var split = key.split("-");
        var str = create_single_attribute_string(key, condition_json);
        conditions.push(str);
      }else{
        console.log("There are something wrong. " + key + " is not a valid key!!")
      }
    }
    return conditions;
};




module.exports.getServices = function (req, res) {
  console.log("In service handler: get services");
  console.log("query: " + JSON.stringify(req.query));
  var conditions = [];
  var queryString = req.query;
  conditions = create_Condition_array(queryString);
  console.log(conditions);


  db.get(conditions, (services) => { 
    res.json(services);
  });
}









/* Check if the json value has the correct variable type fot that attribute key.
 * Parameters:
 *    - key: one of the key of the service.
 *    - value: the value of this key. 
 */
var checkValue_type = (key, value)=>{
   switch(key){
    case "type": case "name": case "dow": case "owner": case "description": case "date": 
      if(typeof(value)!="string"){
        console.log("The value of " + key + " shoulbe be a string, not a " + typeof(value) + "." );
        return false;
      }
      break;
    case "longi": case "lat": 
      if(typeof(value)!="number"){
        console.log("The value of " + key + " shoulbe be a number, not a " + typeof(value) + "." );
        return false;
      }
      break;
   }
   return true;
}


/* Check if this key is valid during the add service process.
 * Parameters:
 *   - key: one of the key that is read from the query. 
 */
var isValidKey_for_add = function (key) {
  if(Create_serviceAttributes.includes(key)){
    return true
  }
  return false;
}; 



/* Loop through the entile json object to check if the json boject is valid for creating a service.
 * Parameters:
 *    - service: the json object which contains all the attribute value to create a service.
 */
var check_serviceJson_valid = (service)=>{

  const keys = Object.keys(service);
  for(var key of keys){
    if(!isValidKey_for_add(key) || !checkValue_type(key, service[key])){
      return false;
    }
  }
  return true;
}




module.exports.addService = function (req, res) {
  console.log("In service handler: add service");
  var service = {};
  //TODO: Get service from request (req) body, and check that it has all the information required
  // The service should be in a JSON object (Details in README), and assigned to the variable service.
  
  var service_json = req.body; // Now we have the JSON object that the client put in the request body
  console.log(service_json);
  // First we want to check if the parameters are valid (Here I only checked if it is type string, but we want to check more than that)
  if (typeof(service_json) === "string") {
    service_json = JSON.parse(service_json);
  }

  if(!check_serviceJson_valid(service_json)){
    console.log("This is not a valid json object for create a service.");
  }

  console.log(service_json);

  db.add(service_json, (id) => {
    res.json(id);
  });

};
