/* This file contains the connection information for testing databases
 */

var serviceDB = {
  host:"ec2-3-13-46-252.us-east-2.compute.amazonaws.com",
  port: 3306,
  user: "remote",
  password: "password", // Change password to mysql server root user"s password
  database: "services_test"
};

var chatDB = {
  host:"ec2-3-13-46-252.us-east-2.compute.amazonaws.com",
  port: 3306,
  user: "remote",
  password: "password", // Change password to mysql server root user"s password
  database: "chat_test",
  charset: "utf8",
  collate: "utf8mb4_unicode_ci"
};

var userDB = {
  host:"ec2-3-13-46-252.us-east-2.compute.amazonaws.com",
  port: 3306,
  user: "remote",
  password: "password", // Change password to mysql server root user"s password
  database: "user_test"
};

module.exports.serviceDB = serviceDB;
module.exports.chatDB = chatDB;
module.exports.userDB = userDB;

