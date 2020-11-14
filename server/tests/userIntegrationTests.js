var request = require("supertest");

module.exports = (server) => {
/* ADD USER TESTS */
  describe.each([
      [{username: "Bob",
        password: "osuBVo8oib-dcs9",
        deviceToken: "bhjiuy76tfhjk-=,p[001-r09328hjm=`=`mv=-0rqev90bh0eberb",
      }, 200, {username: "Bob", password: "osuBVo8oib-dcs9",}]


  ])("Add user", (user, code, expected) => {

    test("returns " + code, async () => {

      var res = await request(server)
        .post("/user")
        .send(user);
    
      expect(res.statusCode).toEqual(code);
      if (code === 200) {
        expect(res.body).toMatchObject(expected);
      }

    });

  });

/* USER LOG IN TESTS */
  describe.each([
      [{username: "Caleb",
        password: "pass",
      }, 200],

      [{username: "Bob",
        password: "Not the password",
      }, 401]

  ])("User Log In", (user, code) => {

    test("returns " + code, async () => {

      var res = await request(server)
        .put("/user/login")
        .send(user);
    
      expect(res.statusCode).toEqual(code);
      if (code === 200) {
        expect(res.body).toMatchObject(user);
      }

    });

  });

};
