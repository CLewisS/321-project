var userHandler = require("../../userHandler/userHandler.js");
jest.mock("../../dbInterface/dbConfig.js");
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


test("Add user: Valid", (done) => {

  var req = {body: { username: "MRAK",
                     password: "dwiahsdfvlknsdvd",
                     deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"
                   }};

  var res = { 
    json(input) {
      try {
        expect(this.code).toBeUndefined();
        expect(input).toMatchObject({username: "MRAK", password: "dwiahsdfvlknsdvd"});
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

  userHandler.addUser(req, res);

});

test("User Add: Invalid Attribute", (done) => {

  var req = {body: { user: "MRAK",
                     password: "dwiahsdfvlknsdvd",
                     deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"
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

  userHandler.addUser(req, res);

});

test("User Add: Invalid Type", (done) => {

  var req = {body: { username: "MRAK",
                     password: 5150,
                     deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"
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

  userHandler.addUser(req, res);

});

test("Add user: null deviceToken", (done) => {

  var req = {body: { username: "MRAK",
                     password: "dwiahsdfvlknsdvd",
                     deviceToken: ""
                   }};

  var res = { 
    json(input) {
      try {
        expect(this.code).toBeUndefined();
        expect(input).toMatchObject({username: "MRAK", password: "dwiahsdfvlknsdvd"});
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

  userHandler.addUser(req, res);

});
