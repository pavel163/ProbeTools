function addPreference(data){
    $.post( "/addPreference", data)
                .done(function(data) {
                   $("table tbody").append('<tr><td>'+data.key+'</td><td>'+data.value+'</td><td>'+data.type+'</td></tr>');
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