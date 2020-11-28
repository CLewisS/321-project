jest.mock("../dbInterface/dbConfig.js");
var testDb = require("./testDbSetup.js");

var integrationTests = require("./integrationTests/integrationTests.js");
var unitTests = require("./unitTests/unitTests.js");

var server = require("../requestManager");


beforeAll((done) => {
  var cb = function() {
    var count = 0;
    return () => {
      if (count === 1) {
        done();
      } else {
        count++;
      }
    };
  };

  var callback = cb();
  testDb.initServiceDb(callback);
  testDb.initUserDb(callback);
});

afterAll((done) => {
  
  var cb = function() {
    var count = 0;
    return () => {
      if (count === 1) {
        done();
      } else {
        count++;
      }
    };
  };

  server.close();

  var callback = cb();
  testDb.tearDownServiceDb(callback);
  testDb.tearDownUserDb(callback);
});


describe("Unit-Tests", () => {
  unitTests();
});

describe("Integration-Tests", () => {
  integrationTests(server);
});
