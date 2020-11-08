
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

  if ((stringAttributes.includes(key) && typeof(value) != "string") ||
      (numberAttributes.includes(key) && typeof(value) != "number")) {
    return false;
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
    if(!serviceAttributes.includes(key) || !isCorrectType(key, service[String(key)])){
      return false;
    }
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

  if (!serviceIsValid(service)) {
    throw "Service is invalid";
  }

  return service;
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
  } else {
    throw key + " is not a valid key";
  }

  return str;
};


var hasValidComparator = function(split) {
  if(split.length !== 2 && singleValConditions.includes(split[0])) {
    return true;
  } else if (validComps.includes(split[1])) {
    return true;
  }

  return false;
};

var isValidCondition = function (condition) {
  var split = condition.split("-");
  
  if (searchConditions.includes(split[0]) && hasValidComparator(split)) {
    return true;
  } 

  return false;
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
    if (isValidCondition(key)) {
      var str = createConditionString(key, conditions);
      conditionStrs.push(str);
    } else {
      throw key + " is not a valid key";
    }
  }

  return conditionStrs;
};








module.exports.getConditionsFromQuery = function(queryString) {

  return createConditionsArray(queryString);

};
