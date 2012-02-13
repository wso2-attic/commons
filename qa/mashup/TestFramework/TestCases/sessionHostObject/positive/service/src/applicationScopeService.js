this.serviceName = "ApplicationScopeService";
this.scope="application";
var key = "number";
function putValue(param){
    session.put(key,200);
    return <success/>;
}

function getValue(param){
    var number = session.get(key);
    return number;
}

function removeValue(param){
    var number = session.remove(key);
    return <success/>;
}

function clearSession(param){
    session.clear();
    return <success/>;
}