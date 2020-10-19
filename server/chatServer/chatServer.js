
var db = require('../dbInterface/chatDB.js');

module.exports.getMessages = function(req, res) {
  console.log('In chat server');
 
  // This is just a placeholder for debugging
  //*****************************************
  var user1 = 'b';
  var user2 = 'c';
  var newest = 4;
  //*****************************************

  db.get(user1, user2, newest, (messages) => {
    res.json(messages);
  });
};

module.exports.addMessage = function(req, res) {
  console.log('In chat server');

  // This is just a placeholder for debugging
  //*****************************************
  var message = {
    user1: 'b',
    user2: 'c',
    time: '2020-10-11 12:23:45',
    content: 'This is most definitely a message!!!'
  };
  //*****************************************

  db.add(message, (id) => {
    res.json(id);
  });
};
