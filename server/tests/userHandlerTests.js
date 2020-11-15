var userHandler = require("../userHandler/userHandler.js");
jest.mock("../dbInterface/dbConfig.js");
var testDb = require("./testDbSetup.js");


module.exports = function () {

/////////////////////ADD TESTING
    describe.each([
        [{body:{username: "MRAK", password: "dwiahsdfvlknsdvd", deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"}},
          200, {username:"MRAK", password:"dwiahsdfvlknsdvd"}, "Add user: valid"],

        [{body:{user: "MRAK", password: "dwiahsdfvlknsdvd", deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"}},
          400, {}, "Add user: Invalid Attribute"],

        [{body:{username: "MRAK", password: 2343233, deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"}},
          400, {}, "Add user: Invalid Type"],

        [{body:{username: "Joe", password: "word"}},
          200, {username: "Joe", password: "word"}, "Add user: no deviceToken"],

        [{body:{username: "Alice", password: "12345", deviceToken: ""}},
          200, {username:"Alice", password:"12345"}, "Add user: Null deviceToken"],

        [{body:{username: "Alice", password: "12345", deviceToken: "thththt"}},
          403, {code: 403, message:"USER_ALREADY_EXISTS"}, "Add user: Duplicate"]

    ])("Add user", (req, code, expected,  name) => {
    
      test( name, (done) => {
    
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
      
        userHandler.addUser(req, res);
      });
    
    });

  

/////////////////////UPDATE TESTING
    describe.each([
      [{body:{username: "MRAK", password: "newpasswordMRAK", deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"}},
        200, {username:"MRAK", password:"newpasswordMRAK"}, "Update user: valid"],

      [{body:{user: "MRAK", password: "dwiahsdfvlknsdvd", deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"}}, 
	      400, {}, "Update user: Invalid Attribute"],

      [{body:{username: "MRAK", password: 2343233, deviceToken: "jfuvrdkopki./fv;jpobycvu_)-788gkdfl;.gdnblgo325v436bw5q4y4-"}},
        400, {}, "Update Add: Invalid Type"],

      [{body:{username: "Joe", password: "pass"}},
        200, {username: "Joe", password: "pass"}, "Update user: no deviceToken"],

      [{body:{username: "Alice", password: "newpasswordAlice", deviceToken: ""}},
        200, {username:"Alice", password:"newpasswordAlice"}, "Update user: Null deviceToken"]

  ])("Update user", (req, code, expected,  name) => {
  
    test( name, (done) => {
  
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
    
      userHandler.updateUser(req, res);
    });
  
  });




/////////////////////LOGIN TESTING
  describe.each([
    [{body:{username: "MRAK", password: "newpasswordMRAK"}},
      200, {username:"MRAK", password:"newpasswordMRAK"}, "Login check: valid"],

    [{body:{user: "MRAK", password: "newpasswordMRAK"}},
      400, {}, "Login check: Invalid Attribute"],

    [{body:{username: "MRAK", password: 2343233}},
      400, {}, "Login check: Invalid Type"],

    [{body:{ username: "Alice", password: "12345"}},
      401, {code: 401, message: "Username and password aren't a valid pair"}, "Login check: wrong password"]

  ])("Login check", (req, code, expected,  name) => {
  
    test( name, (done) => {
  
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
    
      userHandler.loginCheck(req, res);
    });
  
  });
  
  
  
  
  /////////////////////DELETE TESTING
  describe.each([
    [{query:{username: "MRAK"}},
      200, {}, "Delete user: valid"],
  
    [{query:{user: "MRAK"}},
      400, {}, "Delete user: Invalid Attribute"]
  
  ])("Delete user", (req, code, expected,  name) => {
  
    test( name, (done) => {
    
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
    
      userHandler.deleteUser(req, res);
    });
  
  });

};
