// This array simulates local in-memory database
var data = {"todos":[]};
var maxSize = 20;
var StandardResult = Java.type("org.cricketmsf.in.http.StandardResult");

var processRequest = function (request) {

    if(request.method === "GET"){
        return doGet(request);
    }
    if(request.method === "POST"){
      return doPost(request);
    }
    if(request.method === "UPDATE"){
        return doUpdate(request);
    }
    if(request.method === "DELETE"){
        return doDelete(request);
    }
};

/*
 * Get all todo objects from todos array
 */
var doGet = function(request){
    var result = new StandardResult();
    result.buildPayload(JSON.stringify(data));
    result.code=200;
    return result;
}

/*
 * Create a new todo object from request parameters and add to todos array
 */
var doPost = function(request){
    var todo = {
        "name":"",
        "description":"",
        "done":1
    };
    
    todo.name=request.parameters.name;
    todo.description = request.parameters.description;
    todo.done = request.parameters.done;
    if(data.todos.length>maxSize){
        data.todos.splice(0,1);
    }
    data.todos.push(todo);

    var result = new StandardResult();
    result.code=201;
    return result;
}

var doUpdate = function(request){
    var result = new StandardResult();
    result.code=200; //TODO
    return result;
}

var doDelete = function(request){
    var result = new StandardResult();
    result.code=200; //TODO
    return result;
}



