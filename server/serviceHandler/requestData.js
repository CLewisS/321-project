
var serviceAttributes = ["name", "date", "dow", "time", "lat", "longi", "type", "owner", "description"];
var searchConditions = ["name", "date", "dow", "time", "lat", "longi", "type", "owner"];
var stringAttributes = ["name", "date", "dow", "time", "type", "owner", "description"];
var numberAttributes = ["lat", "longi"];
var singleValConditions= ["name", "dow", "type", "owner"];

module.exports.getConditionsFromQuery = function(queryString) {

  return createConditionsArray(queryString);

};

module.exports.getServiceFromReq = function(body) {
  var service;
  if (typeof(body) === "string") {
    service = JSON.parse(body);
  } else {
    service = body;
  }

  if (!serviceIsValid(service)) {
    throw "Service is invalid";
  }

  return service;
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

  if (split.length == 2) {
    var comparator = split[1];
  }

  if (singleValConditions.includes(attribute)) {

    str = attribute + "='" + conditions[key] + "'";

  } else if (comparator === "max") {

     if (numberAttributes.includes(attribute)) {

       str = attribute + " <= " + conditions[key];

     } else {

       str = attribute + " <=" + "'" + conditions[key] + "'";

     }

  } else if (comparator === "min") {

     if (numberAttributes.includes(attribute)) {

       str = attribute + " >= " + conditions[key];

     } else {

       str = attribute + " >=" + "'" + conditions[key] + "'";

     }

  } else {

    throw key + " is not a valid key";

  }

  return str;
};

var isValidCondition = function (condition) {
  var split = condition.split("-");
  if ( (split.length == 2 && searchConditions.includes(split[0]) && (split[1] === "max" || split[1] == "min")) 
      || (split.length == 1 && searchConditions.includes(split[0])) ) {

    console.log("valid");
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
      console.log("There is something wrong. " + key + " is not a valid key!!")
      throw key + " is not a valid key";
    }
  }

  return conditionStrs;
};


/* Check if the json value has the correct variable type fot that attribute key.
 * Parameters:
 *    - key: one of the key of the service.
 *    - value: the value of this key. 
 */
var isCorrectType = function (key, value) {

  if (stringAttributes.includes(key) && typeof(value) != "string") {

    console.log("The value of " + key + " shoulbe be a string, not a " + typeof(value) + "." );
    return false;

  } else if (numberAttributes.includes(key) && typeof(value) != "number") {

    console.log("The value of " + key + " shoulbe be a number, not a " + typeof(value) + "." );
    return false;

  } 

  return true;

}


/* Checks if service object properties are valid.
 * Parameters:
 *    - service: An object which contains all the attribute values to create a service
 */
module.exports.serviceIsValid = function (service) {

  const keys = Object.keys(service);
  for(var key of keys){
    if(!serviceAttributes.includes(key) || !isCorrectType(key, service[key])){
      return false;
    }
  }

  return true;
}


/* Checks if service object properties are valid.
 * Parameters:
 *    - service: An object which contains all the attribute values to create a service
 */
var serviceIsValid = function (service) {

  const keys = Object.keys(service);
  for(var key of keys){
    if(!serviceAttributes.includes(key) || !isCorrectType(key, service[key])){
      return false;
    }
  }

  return true;
}