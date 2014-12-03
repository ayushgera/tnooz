var http = require('http'),
      fs = require('fs'),
     url = require('url'),
 choices = ["hello world", "goodbye world"];

http.createServer(function(request, response){
    var path = url.parse(request.url).pathname;
    if(path=="/getstring"){
        console.log("request recieved");
        var string = choices[Math.floor(Math.random()*choices.length)];
        console.log("string '" + string + "' chosen");
        response.writeHead(200, {"Content-Type": "text/plain"});
        response.end(string);
        console.log("string sent");
    }else{
        //fs.readFile('./index.html', function(err, file) {  
          //  if(err) {  
                // write an error response or nothing here  
          //      return;  
          //  }  
          //  response.writeHead(200, { 'Content-Type': 'text/html' });  
          //  response.end(file, "utf-8");  
        //});
        console.log("request.method="+request.method);

            request.on("data",function(){

                console.log("data:"+data);
            });

            request.on("end",function(){

                console.log("end");
            });
    }
}).listen(8001);
/*var http= require('http');
http.createServer(function(request,response){
    console.log("request.method="request.method);

    request.on("data",function(){

        console.log("data:"+data);
    });

    request.on("end",function(){

        console.log("end");
    });    
});*/
console.log("server initialized");