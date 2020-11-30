var serviceHandler = require("../../serviceHandler/serviceHandler.js");

module.exports = function () {
  
  describe.each([
      [{body: { id: 123,
        name: "service",
        dow: "Monday",
        date: "2020-5-17",
        time: "12:57:33",
        lat: 49.56911,
        longi: 123.456,
        owner: "Caleb",
        type: "food",
        description: "This is a description",
        maxCapacity: 3
      }} , 200, {id:3}, "Service Add: valid"] ,
  
      [{body: { id: 123,
        title: "service 2",
        dow: "Monday",
        date: "2020-4-17",
        time: "12:57:33",
        lat: 49.56911,
        longi: 123.456,
        owner: "Caleb",
        type: "food",
        description: "This is a description",
        maxCapacity: 2
      }} , 400 , {}, "Service Add: Invalid Attribute"],
  
      [{body: { id: 123,
          name: 15,
          dow: "Monday",
          date: "2020-3-17",
          time: "12:57:33",
          lat: 49.56911,
          longi: 123.456,
          owner: "Caleb",
          type: "food",
          description: "This is a description",
          maxCapacity: 1
        }} , 400, {}, "Service Add: Invalid Type"]
  
  
  ])("Add service", (req, code, expected,  name) => {
  
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
      
        status(input) {
          this.code = input;
          return this;
        }
      };
    
      serviceHandler.addService(req, res);
    });
  
  });
  
    
   
  describe.each([
      [{query:{name:"service", owner:"Caleb"}}, "service", 200, "Valid"],

      [{query:{"lat-max": 50, "lat-min": 40, "longi-min": 111, "date-min": "2020-6-6", "time-max": "23:55:55"}}, "food service", 200, "Valid"],

      [{query:{"lat-max": 50, "lat-min": 40, "loni-min": 111, "date-min": "2020-6-6"}}, "", 400, "Invalid attribute"],

      [{query:{"lat-man": 50, "lat-min": 40, "longi-min": 111, "date-min": "2020-6-6", "time-max": "23:55:55"}}, "", 400, "Invalid comp"],

      [{query:{"lat": 50, "lat-min": 40, "longi-min": 111, "date-min": "2020-6-6", "time-max": "23:55:55"}}, "", 400, "Invalid comp"],

      [{query:{"lat-man": "hi", "time-max": "23:55:55"}}, "", 400, "Incorrect type"],
  
      [{query:{name:"service", label:"Caleb"}}, "", 400, "Service get: invalid query key"]
  
  ])("Get service", (req, expected, code, name) => {
  
    test( name, (done) => {
  
      var res = { 
        json(input) {
          try {
            if (code === 200) {
              expect(this.code).toBeUndefined();
              expect(input[0].name).toBe(expected);
              expect(Object.keys(input[0])).toHaveLength(12);
            }else{
              expect(this.code).toEqual(code);
            }
  
            done();
          } catch (err) {
            done(err);
          }
        },
      
        status(input) {
          this.code = input;
          return this;
        }
      };
    
      serviceHandler.getServices(req, res);
    });
  
  });
  
  
  
  describe.each([
  
      [{ query:{id:"3"},
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
               }},
        200, "Service update: Valid"],

      [{ query:{service:"3"},
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
                }},
        400,"Service update: inValid query key"],

      [{query:{id:"3"},
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
                }},
        400, "Service update: inValid body key"],

      [{query:{id:"3"},
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
                }},
        400, "Service update: inValid body value type"],
  
  ])("Update service", (req, code, name) => {
  
    test( name, (done) => {
  
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
      };
    
      serviceHandler.updateService(req, res);
    });
  
  });
  
  
  

  describe.each([
    [{body:{username: "Caleb", serviceID: 1}}, 200],

    [{body:{user: "Caleb", serviceID: 1}}, 500],
    
    [{body:{username: "Cale", serviceID: 67}}, 500]

  ])("RSVPs", (req, code) => {    
    test(JSON.stringify(req.body) , (done) => {
    
      var res = { 
        json(input) {
          try {
            if (code === 200) {
              expect(this.code).toBeUndefined();
              expect(input.id).toBeDefined();
            }else{
              expect(this.code).toEqual(code);
            }
            done();
          } catch (err) {
            done(err);
          }
        },
      
        status(input) {
          this.code = input;
          return this;
        }
      };
    
      serviceHandler.receiveService(req, res);
    });
  });

  describe.each([
    [{query: {username: "Caleb", status: "receive"}}, 200, "food service"],

    [{query: {username: "Cale", status: "receive"}}, 500],

    [{query: {user: "Caleb", status: "receive"}}, 500]
  ])("Get RSVPs", (req, code, name) => {    
    test(JSON.stringify(req.query) , (done) => {
    
      var res = { 
        json(input) {
          try {
            if (code === 200) {
              expect(this.code).toBeUndefined();
              expect(input[0].name).toBe(name);
              expect(Object.keys(input[0])).toHaveLength(12);
            }else{
              expect(this.code).toEqual(code);
            }
            done();
          } catch (err) {
            done(err);
          }
        },
      
        status(input) {
          this.code = input;
          return this;
        }
      };
    
      serviceHandler.getReceivedServices(req, res);
    });
  });




  describe.each([
    [{query:{id:"3"}}, {}, 200, "Service delete: Valid"],
  
    [{query:{service:"3"}}, {}, 400, "Service delete: invalid query"]
   
  ])("Delete service", (req, expected, code, name) => {
  
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
      
        status(input) {
          this.code = input;
          return this;
        }
      };
    
      serviceHandler.deleteService(req, res);
    });
  
  });

};
