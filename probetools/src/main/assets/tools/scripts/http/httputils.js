var requestId;
var responseId;

function loadRequestData(){
    requestId = setInterval(function(){
    $.get( "/http/request")
            .done(function(data) {
                $('#request').text(data);
            })
            .fail(function(){

            });

    }, 1500)
}

function loadResponseData(){
    responseId = setInterval(function(){
        $.get( "/http/response")
                .done(function(data) {
                    $('#response').text(JSON.stringify(data.headers, undefined, 2));
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