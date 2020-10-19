
var db = require('../dbInterface/chatDB.js');

module.exports.getMessages = function(req, res) {
  console.log('In chat server');
 
  var message;

  db.get(conditions, (id) => {
    res.json(id);
  });
};

module.exports.addMessage = function(req, res) {
  console.log('In chat server');

  // This is just a placeholder for debugging
  //*****************************************
  var message = {
    user1: 'a',
    user2: 'b',
    time: '2020-10-11 12:23:45',
    content: 'THis is most definitely a message!!!'
  };
  //*****************************************

  db.add(message, (id) => {
    res.json(id);
  });
};
