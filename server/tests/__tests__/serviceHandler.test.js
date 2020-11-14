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



test("Service get: Valid", (done) => {

  var req = {query: { name:"service", owner:"Caleb" }};

  var expected = [{ id:3,
                   name: "service",
                   dow: "Monday",
                   date: "2020-05-17T07:00:00.000Z",
                   time: "12:57:33",
                   lat: 49.56911,
                   longi: 123.456,
                   owner: "Caleb",
                   type: "food",
                   description: "This is a description"
                }];
  
  var res = { 
    json(input) {
      try {
        expect(this.code).toBeUndefined();
        expect(input).toMatchObject(expected);
        done();
      } catch (err) {
        done(err);
      }
    },
  
    code: undefined,
  
    status(input) {
      this.code = null;
      return this;
    }
  }

  serviceHandler.getServices(req, res);

});



test("Service get: invalid query key", (done) => {

  var req = {query: { id: "1",
                     label:'food'
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

  serviceHandler.getServices(req, res);

});

//query is always a string, so i do not think we need to check is the value in a query string.
// test("Service get: invalid query value type", (done) => {

//   var req = {query: { id: "1",
//                      name:12
//                }};
  
//   var res = { 
//     json(input) {
//       try {
//         console.log( 'qweqweqweaweada  '+ this.code + "   asdasdasdasdas")
//         expect(this.code).toBe(400);
//         done();
//       } catch (err) {
//         done(err);
//       }
//     },
  
//     code: undefined,
  
//     status(input) {
//       this.code = input;
//       return this;
//     }
//   }

//   serviceHandler.getServices(req, res);

// });





test("Service update: Valid", (done) => {
  
  var req = {
    
    query:{id:"1"},

    body: {     
                 name: "service",
                 dow: "Monday",
                 date: "2020-5-17",
                 time: "12:57:33",
                 lat: 49.56911,
                 longi: 123.456,
                 owner: "CalebUpdated",
                 type: "food",
                 description: "This is a updaetd description"
               }};
  
  var res = { 
    json(input) {
      try {
        expect(this.code).toBe(200);
        done();
      } catch (err) {
        done(err);
      }
    },
  
    code: 200,
  
    status(input) {
      this.code = input;
      return this;
    }
  }

  serviceHandler.updateService(req, res);

});



test("Service update: inValid query key", (done) => {
  
  var req = {
    
    query:{idnum:"1"},

    body: {      id: 123,
                 name: "service",
                 dow: "Monday",
                 date: "2020-5-17",
                 time: "12:57:33",
                 lat: 49.56911,
                 longi: 123.456,
                 owner: "CalebUpdated",
                 type: "food",
                 description: "This is a updaetd description"
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
  
    code: 400,
  
    status(input) {
      this.code = input;
      return this;
    }
  }

  serviceHandler.updateService(req, res);

});


test("Service update: inValid body key", (done) => {
  
  var req = {
    
    query:{id:"1"},

    body: {      id: 123,
                 nameWRONG: "service",
                 dow: "Monday",
                 date: "2020-5-17",
                 time: "12:57:33",
                 lat: 49.56911,
                 longi: 123.456,
                 owner: "CalebUpdated",
                 type: "food",
                 description: "This is a updaetd description"
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
  
    code: 400,
  
    status(input) {
      this.code = input;
      return this;
    }
  }

  serviceHandler.updateService(req, res);

});



test("Service update: inValid body value type", (done) => {
  
  var req = {
    
    query:{id:"1"},

    body: {      id: 123,
                 name: "service",
                 dow: "Monday",
                 date: "2020-5-17",
                 time: "12:57:33",
                 lat: "49.56911",
                 longi: "123.456",
                 owner: "CalebUpdated",
                 type: "food",
                 description: "This is a updaetd description"
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
  
    code: 400,
  
    status(input) {
      this.code = input;
      return this;
    }
  }

  serviceHandler.updateService(req, res);

});




test("Service delete: valid", (done) => {

  var req = {query: { id: "1"}};
  
  var res = { 
    json(input) {
      try {
        expect(this.code).toBe(200);
        done();
      } catch (err) {
        done(err);
      }
    },
  
    code: 200,
  
    status(input) {
      this.code = input;
      return this;
    }
  }

  serviceHandler.deleteService(req, res);

});



test("Service delete: invalid query", (done) => {

  var req = {query: { service: "1"}};
  
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

  serviceHandler.deleteService(req, res);

});




describe("User Handler Tests", () => {
  userHandlerTests();
});

describe("Integration Tests", () => {
  integrationTests(server);
});
