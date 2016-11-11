function addPreference(){
    $.post( "/addPreference")
                .done(function(data) {
                          $("table tbody").append('<tr><td>'+data.key+'</td><td>'+data.value+'</td><td>'+data.type+'</td></tr>');
            });
}

function loadPreferences(preferences){
    for(var i=0; i<preferences.length; i++) {
        $("table tbody").append('<tr><td>'+preferences[i].key+'</td><td>'+preferences[i].value+'</td><td>'+preferences[i].type+'</td></tr>');
    }
}