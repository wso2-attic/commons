Tryit = new function () {
    var viewurl = "/tryit/tryit.jag";
    var pathArray = window.location.pathname.split( '/' );
    var secondLevelLocation = pathArray[1];
   console.log('xx'+secondLevelLocation);
   if(secondLevelLocation != "tryitview.jag" && secondLevelLocation != "tryitframe.jag" ){
   viewurl = "/"+secondLevelLocation +"/"+ viewurl;}
    this.call = function () {
        var arg = editor.getValue();
        console.log('Hii' + arg);
        var qString = $('#qString').val();
        var htmlResult;
        TryitUtil.makePost(viewurl,
            "inputstream=" + encodeURIComponent(arg) + "&" + qString, function (html) {
                htmlResult = html;
                $('#output').contents().find('html').html(htmlResult);
            });
        //$('#output').html(htmlResult);

        //return htmlResult;
    };
    this.test = function () {

        var xx = editor.getValue();
        console.log(xx + 'Hii' + arg);
        $('#output').html('<p>out' + arg + '</p>');
        return arg;
    };
}
