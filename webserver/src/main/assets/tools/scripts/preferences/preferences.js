function addPreference(data){
    $.post( "/addPreference", data)
                .done(function(data) {
                   $("table tbody").append('<tr><td id="'+data.key +'_key"><a class="waves-effect" id="'+data.key +'" href="#medit" title="Edit"><i class="material-icons">mode_edit</i></a>'+data.key+'</td><td id="'+data.key +'_value">'+data.value+'</td><td id="'+data.key +'_type">'+data.type+'</td></tr>');
                           $('#'+data.key).on('click', function(){
                                       $("#pref_key_edit").val(this.id)
                           })
                })
                .fail(function(){
                    alert( "Wrong type" );
                });
}

function loadPreferences(preferences){
    for(var i=0; i<preferences.length; i++) {
        $("table tbody").append('<tr><td id="'+preferences[i].key +'_key"><a class="waves-effect" id="'+preferences[i].key +'" href="#medit" title="Edit"><i class="material-icons">mode_edit</i></a>'+preferences[i].key+'</td><td id="'+preferences[i].key +'_value">'+preferences[i].value+'</td><td id="'+preferences[i].key +'_type">'+preferences[i].type+'</td></tr>');
        $('#'+preferences[i].key).on('click', function(){
                    $("#pref_key_edit").val(this.id)
        })
    }
}

function editPreference(data){
    $.post( "/addPreference", data)
                .done(function(data) {
                   $('#'+data.key+ '_value').text(data.value)
                   $('#'+data.key+ '_type').text(data.type)
                })
                .fail(function(){
                    alert( "Wrong type" );
                });
}