var userHandlerTests = require("./userHandlerTests.js");
var serviceHandlerTests = require("./serviceHandlerTests.js");

module.exports = function (server) {
  describe("Service-Module-Unit", () => {
    serviceHandlerTests();
  });

  describe("User-Module-Unit", () => {
    userHandlerTests();
  });
};
