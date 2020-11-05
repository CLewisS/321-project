
var serviceAttributes = ["name", "date", "dow", "time", "lat", "longi", "type", "owner", "description"];
var searchConditions = ["id", "name", "date", "dow", "time", "lat", "longi", "type", "owner"];
var stringAttributes = ["name", "date", "dow", "time", "type", "owner", "description"];
var numberAttributes = ["id", "lat", "longi"];
var singleValConditions= ["id", "name", "dow", "type", "owner"];





/* Checks if service object properties are valid.
 * Parameters:
 *    - service: An object which contains all the attribute values to create a service
 */
var serviceIsValid = function (service) {
  const keys = Object.keys(service);
  for(var key of keys){
    if(!serviceAttributes.includes(key) || !isCorrectType(key, service[key])){
  console.log("Is valid " + key);
      return false;
    }
  }

  return true;
}

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

  // console.log("get from req " + JSON.stringify(service));
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

  if (split.length === 2) {
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

    // console.log("The value of " + key + " shoulbe be a string, not a " + typeof(value) + "." );
    return false;

  } else if (numberAttributes.includes(key) && typeof(value) != "number") {

    // console.log("The value of " + key + " shoulbe be a number, not a " + typeof(value) + "." );
    return false;

  } 

  return true;

};






module.exports.getConditionsFromQuery = function(queryString) {

  return createConditionsArray(queryString);

};