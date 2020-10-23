
const https = require("https");
const { resolve } = require("path");

const pro = ()=>{
    return new Promise((resolve, reject)=>{
        https.get("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY", resp=>{
            let data = "";
            
            //read json content chunk by chunk
            resp.on("data", chunk => {
                data += chunk;
            });
            
            //when it is at the end of json, parse it to json object
            resp.on("end", () => {
                resolve(JSON.parse(data))
                // console.log(json_data);
            });
            
        }).on("error", err=>{
            reject("Error:" + err.message);
        });
    })
}


pro().then((result)=>{
    //console.log(result.copyright)
    console.log(JSON.stringify(result));
}).catch((e)=>{
    console.log(e)
})




//  await;
// https.get("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY", resp=>{
//       let data = "";
      
//       //read json content chunk by chunk
//       resp.on("data", chunk => {
//           data += chunk;
//       });
       
//        //var json_data;
//       //when it is at the end of json, parse it to json object
//       resp.on("end", () => {
//         var json_data = JSON.parse(data); 
//         console.log(json_data);
//         j = json_data;
       
//       });
     
//   }).on("error", err=>{
//       console.log("Error:" + err.message);
//   });


//   console.log(j);