const userAttributes = ["username","password","deviceToken","servicesPosted","servicesUsed"];
const userStringAttributes = ["username","password","deviceToken"];
const userListAttributes =  ["servicesPosted","servicesUsed"];


var checkType = function(key, value){
    if(userStringAttributes.includes(key) && typeof(value)=="string"){
        return true;
    } 
    if(userListAttributes.includes(key) && typeof(value)=="object"){
       return true;
    }
    return false;
}



module.exports.checkUserInfo = function (user){
  
    var keys = Object.keys(user);
    for (var key of keys) {
        if (!userAttributes.includes(key)) {
          throw key + " is not a valid key";
        }
        if (!checkType(key,user[key])) {
            throw "The value of the " + key + " has the wrong type.";
          }
      }
    return true;
}


