$.urlParam = function() {
    var url = window.location.href;
    return url.split("/jsondocui")[0]+"/jsondoc";
}