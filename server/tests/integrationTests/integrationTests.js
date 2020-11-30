var serviceIntegration = require("./serviceIntegrationTests.js");
var userIntegration = require("./userIntegrationTests.js");

module.exports = function (server) {
  describe("Service-Module-Integration", () => {
    serviceIntegration(server);
  });

  describe("User-Module-Integration", () => {
    userIntegration(server);
  });
};
