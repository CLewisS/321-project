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

describe.each([
    [
      {body: { id: 123,
      name: "service",
      dow: "Monday",
      date: "2020-5-17",
      time: "12:57:33",
      lat: 49.56911,
      longi: 123.456,
      owner: "Caleb",
      type: "food",
      description: "This is a description"
    }} , 200, {id:3}, "Service Add: valid"] ,

    [
      {body: { id: 123,
      title: "service 2",
      dow: "Monday",
      date: "2020-4-17",
      time: "12:57:33",
      lat: 49.56911,
      longi: 123.456,
      owner: "Caleb",
      type: "food",
      description: "This is a description"
    }} , 400 , {}, "Service Add: Invalid Attribute"
    ],

    [
      {body: { id: 123,
        name: 15,
        dow: "Monday",
        date: "2020-3-17",
        time: "12:57:33",
        lat: 49.56911,
        longi: 123.456,
        owner: "Caleb",
        type: "food",
        description: "This is a description"
      }} , 400, {}, "Service Add: Invalid Type"
    ]


])("Add service", (req, code, expected,  name) => {

  test( name, done => {

    var res = { 
      json(input) {
        try {
          if (code === 200) {
            expect(this.code).toBeUndefined();
          
          }else{
            expect(this.code).toEqual(code);
          }

          expect(input).toMatchObject(expected);
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

  
 
describe.each([
    [
      {query:{name:"service", owner:"Caleb"}},
      [{ id:3,
        name: "service",
        dow: "Monday",
        date: "2020-05-17T07:00:00.000Z",
        time: "12:57:33",
        lat: 49.56911,
        longi: 123.456,
        owner: "Caleb",
        type: "food",
        description: "This is a description"
     }],
     200,
     "Service get: Valid"
    ] ,

    [
      {query:{name:"service", label:"Caleb"}},
      {"code": 400, "message": "Condition label is not valid"},
     400,
     "Service get: invalid query key"
    ],

   
])("Get service", (req, expected, code, name) => {

  test( name, done => {

    var res = { 
      json(input) {
        try {
          if (code === 200) {
            expect(this.code).toBeUndefined();
          
          }else{
            expect(this.code).toEqual(code);
          }

          expect(input).toMatchObject(expected);
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

});



describe.each([

    [
      {
        query:{id:"3"},
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
              }
       },
      200,
      "Service update: Valid" 
    ],
    [
      {
        query:{id:"3"},
        body: {     
                id: 123,
                name: "service",
                dow: "Monday",
                date: "2020-5-17",
                time: "12:57:33",
                lat: 49.56911,
                longi: 123.456,
                owner: "CalebUpdated",
                type: "food",
                description: "This is a updaetd description"
              }
       },
      400,
      "Service update: inValid query key"
    ],
    [
      {
        query:{id:"3"},
        body: {     
                id: 123,
                nameWRONG: "service",
                dow: "Monday",
                date: "2020-5-17",
                time: "12:57:33",
                lat: 49.56911,
                longi: 123.456,
                owner: "CalebUpdated",
                type: "food",
                description: "This is a updaetd description"
              }
       },
      400,
      "Service update: inValid body key"
    ],
    [
      {
        query:{id:"3"},
        body: {     
                id: 123,
                name: "service",
                dow: "Monday",
                date: "2020-5-17",
                time: "12:57:33",
                lat: "49.56911",
                longi: "123.456",
                owner: "CalebUpdated",
                type: "food",
                description: "This is a updaetd description"
              }
       },
      400,
      "Service update: inValid body value type"
    ],

])("Update service", (req, code, name) => {

  test( name, done => {

    var res = { 
      json(input) {
        try {
          if (code === 200) {
            expect(this.code).toBe(200);
          
          }else{
            expect(this.code).toEqual(code);
          }

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

});







describe.each([
  [
    {query:{id:"3"}},
    {},
    200,
    "Service delete: Valid"
  ] ,

  [
    {query:{service:"3"}},
    {},
    400,
    "Service delete: invalid query"
  ],

 
])("Delete service", (req, expected, code, name) => {

test( name, done => {

  var res = { 
    json(input) {
      try {
        if (code === 200) {
          expect(this.code).toBeUndefined();
        
        }else{
          expect(this.code).toEqual(code);
        }

        expect(input).toMatchObject(expected);
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

});



 });


describe("User Handler Tests", () => {
  userHandlerTests();
});

describe("Integration Tests", () => {
  integrationTests(server);
});
