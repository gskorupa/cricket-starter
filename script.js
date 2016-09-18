var counter = 0;

var processRequest = function (method, pathExt) {
    
    if(method === "GET"){
        return doGet(pathExt);
    }
    if(method === "POST"){
        return doPost(pathExt);
    }
    if(method === "UPDATE"){
        return doUpdate(pathExt);
    }
    if(method === "DELETE"){
        return doDelete(pathExt);
    }
};

var doGet = function(pathExt){
    counter=counter+1;
    var result = [
        {"a":"1"},
        {"counter": counter},
        {"pathExt": pathExt}
    ];
    return JSON.stringify(result);
}

var doPost = function(pathExt){
    return "Hello from POST!";
}

var doUpdate = function(pathExt){
    return "Hello from UPDATE!";
}

var doDelete = function(pathExt){
    return "Hello from DELETE!";
}

