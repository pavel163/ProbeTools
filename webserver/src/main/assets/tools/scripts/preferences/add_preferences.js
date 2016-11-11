function addPreferences(preferences){
    for(var i=0; i<preferences.length; i++) {
        $("table tbody").append('<tr><td>'+preferences[i].key+'</td><td>'+preferences[i].value+'</td><td>'+preferences[i].type+'</td></tr>');
    }
}