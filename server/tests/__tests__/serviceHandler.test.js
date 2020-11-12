var serviceHandler = require("../../serviceHandler/serviceHandler.js");
//jest.mock("../../dbInterface/serviceDB.js");
jest.mock("../../dbInterface/dbConfig.js");
var db = require("../../dbInterface/serviceDB.js");
var testDb = require("../testDbSetup.js");

beforeEach(async () => {
  await testDb.initServiceDb();
  await testDb.initUserDb();
});

afterEach(async () => {
  await testDb.tearDownServiceDb();
  await testDb.tearDownUserDb();
});

test("Service Add: Get JSON", (done) => {

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
  
/*
  db.add.mockImplementation((service, callback) => {
    expect(service).toMatchObject(expected);
    callback({id: 15});
  });
*/
  var res = {
    json(input) {
      try {
        console.log("Called json");
        console.log("Called status " + JSON.stringify(input));
        expect(this.code).toBe(0);
        expect(input).toMatchObject({id: 1});
        done();
      } catch (err) {
        done(err);
      }
    },
    code: 0,
    status(input) {
      this.code = input;
      return this;
    }
  };

  serviceHandler.addService(req, res);

});
