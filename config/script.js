// This array simulates local in-memory database
var data = {"todos": []};
var maxSize = 20;
var StandardResult = Java.type("org.cricketmsf.in.http.StandardResult");
var uid = 0;

var processRequest = function (request) {

    if (request.method === "GET") {
        return doGet(request);
    }
    if (request.method === "POST") {
        return doPost(request);
    }
    if (request.method === "UPDATE") {
        return doUpdate(request);
    }
    if (request.method === "DELETE") {
        return doDelete(request);
    }
};

/*
 * Get all todo objects from todos array
 */
var doGet = function (request) {
    var result = new StandardResult();
    result.buildPayload(JSON.stringify(data));
    result.code = 200;
    return result;
}

/*
 * Create a new todo object from request parameters and add to todos array
 */
var doPost = function (request) {
    var todo = {
        "id": "",
        "name": "",
        "description": "",
        "done": 1
    };

    todo.id = getUid();
    todo.name = request.parameters.name;
    todo.description = request.parameters.description;
    todo.done = request.parameters.done;
    if (data.todos.length > maxSize) {
        data.todos.splice(0, 1);
    }
    data.todos.push(todo);

    var result = new StandardResult();
    result.code = 201;
    return result;
}

var doUpdate = function (request) {
    var result = new StandardResult();
    result.code = 200; //TODO
    return result;
}

var doDelete = function (request) {
    var result = new StandardResult();
    var id = request.pathExt;
    var ok = false;
    for (var i = data.todos.length - 1; i >= 0; i--) {
        if (data.todos[i].id == id) {
            data.todos.splice(i, 1);
            ok = true;
            break;
        }
    }
    if(ok){
        result.code = 200;
    }else{
        result.code = 404;
    }
    return result;
}

var getUid = function () {
    return uid++;

}



