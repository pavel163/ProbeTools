function loadDatabases(){
        preloadOn();
        $.get( "/loadDatabases")
            .done(function(data) {
                preloadOff();
                for (i = 0; i < data.length; i++) {
                    $('#databases').append($('<li>').attr('id', data[i]).append($('<a>').attr("href", "database?db="+data[i]).append(data[i])));
                }
             })
            .fail(function(){
                preloadOff()
            });
}