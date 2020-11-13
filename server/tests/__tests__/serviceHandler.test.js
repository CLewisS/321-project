var serviceHandler = require("../../serviceHandler/serviceHandler.js");
jest.mock("../../dbInterface/dbConfig.js");
var db = require("../../dbInterface/serviceDB.js");
var testDb = require("../testDbSetup.js");

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

  var callback = cb();
  testDb.tearDownServiceDb(callback);
  testDb.tearDownUserDb(callback);
});


test("Service Add: Valid", (done) => {

  var req = {body: { id: 123,
                 name: "A service",
                 dow: "Monday",
                 date: "2020-10-17",
                 time: "12:57:33",
                 lat: 49.56911,
                 longi: 123.456,
                 owner: "Caleb",
                 type: "food",
                 description: "This is a description"
               }};

  var expected = {
                   name: "A service",
                   dow: "Monday",
                   date: "2020-10-17",
                   time: "12:57:33",
                   lat: 49.56911,
                   longi: 123.456,
                   owner: "Caleb",
                   type: "food",
                   description: "This is a description"
                };
  
  var res = { 
    json(input) {
      try {
        expect(this.code).toBeUndefined();
        expect(input).toMatchObject({id: 1});
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
                 title: "A service",
                 dow: "Monday",
                 date: "2020-10-17",
                 time: "12:57:33",
                 lat: 49.56911,
                 longi: 123.456,
                 owner: "Caleb",
                 type: "food",
                 description: "This is a description"
               }};

  var expected = {
                   name: "A service",
                   dow: "Monday",
                   date: "2020-10-17",
                   time: "12:57:33",
                   lat: 49.56911,
                   longi: 123.456,
                   owner: "Caleb",
                   type: "food",
                   description: "This is a description"
                };
  
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
                 date: "2020-10-17",
                 time: "12:57:33",
                 lat: 49.56911,
                 longi: 123.456,
                 owner: "Caleb",
                 type: "food",
                 description: "This is a description"
               }};

  var expected = {
                   name: "A service",
                   dow: "Monday",
                   date: "2020-10-17",
                   time: "12:57:33",
                   lat: 49.56911,
                   longi: 123.456,
                   owner: "Caleb",
                   type: "food",
                   description: "This is a description"
                };
  
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
