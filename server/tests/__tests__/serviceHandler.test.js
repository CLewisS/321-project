var serviceHandler = require("../../serviceHandler/serviceHandler.js");
jest.mock("../../dbInterface/serviceDB.js");
var db = require("../../dbInterface/serviceDB.js");


test("Service Add: Get JSON", () => {
  var req = {body: { id: 123,
                 name: "A service",
                 dow: "Monday",
                 date: "2020-10-17",
                 time: "12:57:33",
                 lat: 49.56911,
                 longi: 123.456,
                 owner: "Brendon",
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
                   owner: "Brendon",
                   type: "food",
                   description: "This is a description"
                };
  

  db.add.mockImplementation((service, callback) => {
    expect(service).toMatchObject(expected);
    callback({id: 15});
  });

  var res = {
    data: {},
    json(input) {
        this.data = input;
    }
  };

  serviceHandler.addService(req, res);

  expect(db.add).toHaveBeenCalled();
  expect(res.data).toMatchObject({id: 15});
});
