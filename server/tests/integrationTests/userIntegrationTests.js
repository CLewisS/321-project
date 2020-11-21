var request = require("supertest");

module.exports = (server) => {
/* ADD USER TESTS */
  describe.each([
      [{username: "Bob",
        password: "osuBVo8oib-dcs9",
        deviceToken: "bhjiuy76tfhjk-=,p[001-r09328hjm=`=`mv=-0rqev90bh0eberb",
      }, 200, {username: "Bob", password: "osuBVo8oib-dcs9"}],
      [{name: "mike",
        password: "osuBVo8oib-dcs9",
        deviceToken: "bhjiuy76tfhjk-=,p[001-r09328hjm=`=`mv=-0rqev90bh0eberb",
      }, 400, {}],
      [{username: "brendon",
        password: 123123213,
        deviceToken: "bhjiuy76tfhjk-=,p[001-r09328hjm=`=`mv=-0rqev90bh0eberb",
      }, 400, {}]


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

      [{username: "Caleb",
      password: 12312,
    }, 400],

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



/* UPDATE USER TESTS */
describe.each([
  [{username: "Caleb",
    password: "newPassword",
    deviceToken: "bhjiuy76tfhjk-=,p[001-r09328hjm=`=`mv=-0rqev90bh0eberb",
  }, 200, {username: "Caleb", password: "newPassword"}],
  [{username: "Bob",
    password: 123213,
    deviceToken: "bhjiuy76tfhjk-=,p[001-r09328hjm=`=`mv=-0rqev90bh0eberb",
  }, 400, {}],
  [{name: "Bob",
    password: "newpassword2",
    deviceToken: "bhjiuy76tfhjk-=,p[001-r09328hjm=`=`mv=-0rqev90bh0eberb",
  }, 400, {}]


])("Update user", (user, code, expected) => {

test("returns " + code, async () => {

  var res = await request(server)
    .put("/user")
    .send(user);

  expect(res.statusCode).toEqual(code);
  if (code === 200) {
    expect(res.body).toMatchObject(expected);
  }

});

});




/* DELETE USER TESTS */
describe.each([
  ["?username=Caleb", 200,{}],

  ["?name=Caleb", 400,{code: 400, message: "Expected a username, but didn't get one"}],

])("Delete User", (query, code, expected) => {

test("returns " + code, async () => {

  var res = await request(server)
    .delete("/user" + query);

  expect(res.statusCode).toEqual(code);
  if (code === 200) {
    expect(res.body).toMatchObject(expected);
  }

});

});





};


