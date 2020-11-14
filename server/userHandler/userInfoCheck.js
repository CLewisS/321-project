const userAttributes = ["username","password","deviceToken"];
const userStringAttributes = ["username","password","deviceToken"];



var checkType = function(key, value){
  if(userStringAttributes.includes(key) && typeof(value) !== "string"){
    throw key + "is type " + typeof(value) + " but should be type string";
    return false;
  } 
  return true;
};



module.exports.checkUserInfo = function (user){

  var keys = Object.keys(user);
  for (var key of keys) {
    if (!userAttributes.includes(key)) {
      throw key + " is not a valid key";
    }

    if (!checkType(key, user[String(key)])) {
       return false;
    }
  }
  return true;
};


