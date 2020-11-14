var serviceHandler = require("../../serviceHandler/serviceHandler.js");
jest.mock("../../dbInterface/dbConfig.js");
var db = require("../../dbInterface/serviceDB.js");
var testDb = require("../testDbSetup.js");
var userHandlerTests = require("../userHandlerTests.js");
var integrationTests = require("../integrationTests.js");
var server= require("../../requestManager");


beforeAll((done) => {
  var cb = function() {
    var count = 0;
    return () => {
      if (count == 1) {
        done();
      } else {
        count++;
      }
    }
  };

  var callback = cb();
  testDb.initServiceDb(callback);
  testDb.initUserDb(callback);
});

afterAll((done) => {
  
  var cb = function() {
    var count = 0;
    return () => {
      if (count == 1) {
        done();
      } else {
        count++;
      }
    }
  };

  server.close();

  var callback = cb();
  testDb.tearDownServiceDb(callback);
  testDb.tearDownUserDb(callback);
});

describe("Service Handler tests", () => {

  test("Service Add: Valid", (done) => {
  
    var req = {body: { id: 123,
                   name: "service",
                   dow: "Monday",
                   date: "2020-5-17",
                   time: "12:57:33",
                   lat: 49.56911,
                   longi: 123.456,
                   owner: "Caleb",
                   type: "food",
                   description: "This is a description"
                 }};
    
    var res = { 
      json(input) {
        try {
          expect(this.code).toBeUndefined();
          expect(input).toMatchObject({id: 3});
          done();
        } catch (err) {
          done(err);
        }
      },
    
      code: undefined,
    
      status(input) {
        this.code = input;
        return this;
      }
    }
  
    serviceHandler.addService(req, res);
  
  });
  
  test("Service Add: Invalid Attribute", (done) => {
  
    var req = {body: { id: 123,
                   title: "service 2",
                   dow: "Monday",
                   date: "2020-4-17",
                   time: "12:57:33",
                   lat: 49.56911,
                   longi: 123.456,
                   owner: "Caleb",
                   type: "food",
                   description: "This is a description"
                 }};
    
    var res = { 
      json(input) {
        try {
          expect(this.code).toBe(400);
          done();
        } catch (err) {
          done(err);
        }
      },
    
      code: undefined,
    
      status(input) {
        this.code = input;
        return this;
      }
    }
  
    serviceHandler.addService(req, res);
  
  });
  
  test("Service Add: Invalid Type", (done) => {
  
    var req = {body: { id: 123,
                   name: 15,
                   dow: "Monday",
                   date: "2020-3-17",
                   time: "12:57:33",
                   lat: 49.56911,
                   longi: 123.456,
                   owner: "Caleb",
                   type: "food",
                   description: "This is a description"
                 }};
  
    var res = { 
      json(input) {
        try {
          expect(this.code).toBe(400);
          done();
        } catch (err) {
          done(err);
        }
      },
    
      code: undefined,
    
      status(input) {
        this.code = input;
        return this;
      }
    }
  
    serviceHandler.addService(req, res);
  
  });
});

describe("User Handler Tests", () => {
  userHandlerTests();
});

describe("Integration Tests", () => {
  integrationTests(server);
});
