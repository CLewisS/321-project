var request = require("supertest");
//var app = require("../requestManager");

module.exports = (server) => {
  test("Create service: valid", async () => {
    var res = await request(server)
      .post("/service")
      .send({id: 123,
             name: "A service",
             dow: "Monday",
             date: "2020-10-17",
             time: "12:57:33",
             lat: 49.56911,
             longi: 123.456,
             owner: "Caleb",
             type: "food",
             description: "This is a description"
      });
  
    expect(res.statusCode).toEqual(200);
    expect(res.body).toHaveProperty("id");
    expect(res.body["id"]).toBeDefined();
  });
};
