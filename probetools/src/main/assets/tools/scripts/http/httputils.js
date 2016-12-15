var requestId;
var responseId;

function loadRequestData(){
    requestId = setInterval(function(){
    $.get( "/http/request")
            .done(function(data) {
                 var data = JSON.parse(JSON.stringify(data, undefined, 2));
                 $('#request_header').html('');
                 $('#request_body').html('');
                 $('.brand-logo').html('<i data-activates="slide-out" class="material-icons button-collapse">menu</i>' + data.url);
                 var h = '';
                 for (key in data.headers) {
                    if (data.headers.hasOwnProperty(key)) {
                        h = h + '<p>'+key + " : " + data.headers[key]+'</p>'
                    }
                 }

                 var p = '';
                 for (var i = 0; i < data.query.length; i++) {
                    p = p + '<p>'+data.query[i] + '</p>';
                 }
                 $('#request_header').html(h);
                 $('#request_body').html(p);
            })
            .fail(function(){

            });

    }, 1500)
}

function loadResponseData(){
    responseId = setInterval(function(){
        $.get( "/http/response")
                .done(function(data) {
                    var data = JSON.parse(JSON.stringify(data, undefined, 2));
                    $('#response_data').html('');
                    $('#response_data').html(JSON.stringify(JSON.parse(data.response), undefined, 2));
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