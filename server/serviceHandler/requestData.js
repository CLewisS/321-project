
var serviceAttributes = ["name", "date", "dow", "time", "lat", "longi", "type", "owner", "description"];
var searchConditions = ["id", "name", "date", "dow", "time", "lat", "longi", "type", "owner"];
var stringAttributes = ["name", "date", "dow", "time", "type", "owner", "description"];
var numberAttributes = ["id", "lat", "longi"];
var singleValConditions= ["id", "name", "dow", "type", "owner"];
var validComps = ["min", "max"];


/* Check if the json value has the correct variable type fot that attribute key.
 * Parameters:
 *    - key: one of the key of the service.
 *    - value: the value of this key. 
 */
var isCorrectType = function (key, value) {

  if (stringAttributes.includes(key) && typeof(value) != "string") {
    throw "Expected type String for " + key + ", but got type " + typeof(value); 
  } else if (numberAttributes.includes(key) && typeof(value) != "number") {
    throw "Expected type number for " + key + ", but got type " + typeof(value); 
  } 

  return true;

};



/* Checks if service object properties are valid.
 * Parameters:
 *    - service: An object which contains all the attribute values to create a service
 */
var serviceIsValid = function (service) {

  const keys = Object.keys(service);
  for(var key of keys){

    if(!serviceAttributes.includes(key)) {
      throw key + " is not a valid service attribute";
    }

    isCorrectType(key, service[String(key)]);

  }

  return true;

};

module.exports.serviceIsValid = serviceIsValid;


module.exports.getServiceFromReq = function(body) {

  var service;
  if (typeof(body) === "string") {
    service = JSON.parse(body);
  } else {
    service = body;
  }

  if (service.hasOwnProperty("id")) {
    delete service.id;
  }

  if (serviceIsValid(service)) {
    return service;
  }

};

var createMaxString = function (attribute, conditions, key) {

  var str;

  if (numberAttributes.includes(attribute)) {
    str = attribute + " <= " + conditions[String(key)];
  } else {
    str = attribute + " <=" + "'" + conditions[String(key)] + "'";
  }

  return str;

};

var createMinString = function (attribute, conditions, key) {

  var str;

  if (numberAttributes.includes(attribute)) {
    str = attribute + " >= " + conditions[String(key)];
  } else {
    str = attribute + " >=" + "'" + conditions[String(key)] + "'";
  }

  return str;

};

/* Create a condition string from a query string key value pair 
 * Parameters:
 *   - key: one of the key that is read from the query. 
 *   - conditions: the object read form the url query string.
 */
var createConditionString = function(key, conditions){

  var str;
  var split = key.split("-");
  var attribute = split[0];

  if (singleValConditions.includes(attribute)) {
    str = attribute + "='" + conditions[String(key)] + "'";
  } else if (split[1] === "max") {
    str = createMaxString(attribute, conditions, key);
  } else if (split[1] === "min") {
    str = createMinString(attribute, conditions, key);
  } 

  return str;

};


var hasValidComparator = function(split) {

  if(split.length === 1 && !singleValConditions.includes(split[0])) {
    throw split[0] + " needs to be a max or min value";
  } else if (split.length == 2 && !validComps.includes(split[1])) {
    throw split[0] + " needs to be a max or min value, but was " + split[1];
  }

  return true;

};


var hasCorrectType = function (key, value) {
  if (numberAttributes.includes(key) && isNaN(Number(value))) {
    throw "Expected type number for " + key + ", but got type " + typeof(value); 
  }

  return true;
};


var isValidCondition = function (condition, value) {

  var split = condition.split("-");
  if (!searchConditions.includes(split[0])) {
    throw "Condition " + condition + " is not valid";
  }
  
  return (hasValidComparator(split) && hasCorrectType(split[0], value));

};


/* Create an array of strings. 
 * Each string consists of one key and its corresponding value (ex. "date >= 2020-10-16")
 *
 * Parameters:
 *   - conditions: the object read form the url query string.
 */
var createConditionsArray = function (conditions) {

  const keys = Object.keys(conditions);

  let conditionStrs = [];

  for (var key of keys) {

    if (isValidCondition(key, conditions[String(key)])) {
      var str = createConditionString(key, conditions);
      conditionStrs.push(str);
    } 

  }

  return conditionStrs;

};


module.exports.getConditionsFromQuery = createConditionsArray;
