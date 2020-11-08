const userAttributes = ["username","password","deviceToken"];
const userStringAttributes = ["username","password","deviceToken"];



var checkType = function(key, value){
    if(userStringAttributes.includes(key) && typeof(value)=="string"){
        return true;
    } 
    return false;
};



module.exports.checkUserInfo = function (user){
  
    var keys = Object.keys(user);
    for (var key of keys) {
        if (!userAttributes.includes(key)) {
          throw key + " is not a valid key";
        }
        if (!checkType(key, user[String(key)])) {
            throw "The value of the " + key + " has the wrong type.";
          }
      }
    return true;
};


