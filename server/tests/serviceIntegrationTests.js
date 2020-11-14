var request = require("supertest");
//var app = require("../requestManager");

module.exports = (server) => {
/* ADD SERVICE TESTS */
  describe.each([
      [{id: 123,
             name: "A service",
             dow: "Monday",
             date: "2020-10-17",
             time: "12:57:33",
             lat: 49.56911,
             longi: 123.456,
             owner: "Caleb",
             type: "food",
             description: "This is a description"
      }, 200],

      [{id: 123,
             name: "A service",
             day: "Monday",
             date: "2020-10-17",
             time: "12:57:33",
             lat: 49.56911,
             longi: 123.456,
             owner: "Caleb",
             type: "food",
             description: "This is a description"
      }, 400],

      [{id: 123,
             name: "a service",
             dow: "monday",
             date: 2020,
             time: "12:57:33",
             lat: 49.56911,
             longi: 123.456,
             owner: "caleb",
             type: "food",
             description: "this is a description"
      }, 400],

      [{id: 123,
             name: "a service",
             dow: "monday",
             date: "2020-10-17",
             time: "12:57:33",
             lat: "49.56911",
             longi: 123.456,
             owner: "caleb",
             type: "food",
             description: "this is a description"
      }, 400]


  ])("Add service", (service, code) => {

    test("returns " + code, async () => {

      var res = await request(server)
        .post("/service")
        .send(service);
    
      expect(res.statusCode).toEqual(code);
      if (code === 200) {
        expect(res.body).toHaveProperty("id");
        expect(res.body["id"]).toBeDefined();
      }

    });

  });

/* GET SERVICE TESTS */
  describe.each([
      ["?name=food service", 200, "food service"],

      ["?date-min=2020-9-9&date-max=2020-12-12&lat-min=48&lat-max=51&longi-max=125", 200, "A service"],

      ["?datei-min=2020-9-9&date-max=2020-12-12&lat-min=48&lat-max=51&longi-max=125", 400, ""],

      ["?date-mn=2020-9-9&date-max=2020-12-12&lat-min=48&lat-max=51&longi-max=125", 400, ""],

      ["?lat-max=hi", 400, ""],

      ["?lat=47", 400, ""]

  ])("Get service", (query, code, name) => {

    test("Get " + query, async () => {

      var res = await request(server)
        .get("/service" + query);
    
      expect(res.statusCode).toEqual(code);
      if (code === 200) {
        console.log(res.body);
        expect(res.body[0].name).toBe(name);
        expect(Object.keys(res.body[0])).toHaveLength(10);
      }

    });

  });
};
