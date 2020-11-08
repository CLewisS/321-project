/* This file contains the connection information databases
 */

var serviceDB = {
  host:"localhost",
  user: "root",
  password: "password", // Change password to mysql server root user"s password
  database: "services"
};

var chatDB = {
  host:"localhost",
  user: "root",
  password: "password", // Change password to mysql server root user"s password
  database: "chat",
  charset: "utf8", 
  collate: "utf8mb4_unicode_ci"
};

var userDB = {
  host:"localhost",
  user: "root",
  password: "password", // Change password to mysql server root user"s password
  database: "user"
};

module.exports.serviceDB = serviceDB;
module.exports.chatDB = chatDB;
module.exports.userDB = userDB;
