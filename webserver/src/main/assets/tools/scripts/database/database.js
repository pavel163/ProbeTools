function loadTableNames(){
        $.get( "/loadTableNames")
            .done(function(data) {
                for (i = 0; i < data.length; i++) {
                    $('#tables').append($("<option></option>").text(data[i]))
                }
                $('#tables').material_select()
        })
}