var requestId;
var responseId;

function loadRequestData(){
    requestId = setInterval(function(){
    $.get( "/http/request")
            .done(function(data) {
                 var data = JSON.parse(JSON.stringify(data.headers, undefined, 2));
                 $('#request_header').html('');
                 var d = '';
                 for (key in data) {
                    if (data.hasOwnProperty(key)) {
                        d = d + '<p>'+key + " : " + data[key]+'</p>'
                    }
                 }
                 $('#request_header').html(d);
            })
            .fail(function(){

            });

    }, 1500)
}

function loadResponseData(){
    responseId = setInterval(function(){
        $.get( "/http/response")
                .done(function(data) {
                    var data = JSON.parse(JSON.stringify(data.headers, undefined, 2));
                    $('#response_header').html('');

                    var d = '';
                    for (key in data) {
                        if (data.hasOwnProperty(key)) {
                            d = d + '<p>'+key + " : " + data[key]+'</p>'
                        }
                    }
                    $('#response_header').html(d);
                })
                .fail(function(){

                });

        }, 1500)
}

function clearRequest(){
    clearInterval(requestId)
}

function clearResponse(){
    clearInterval(responseId)
}