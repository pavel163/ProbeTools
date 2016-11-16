function loadTableNames(){
        $.get( "/loadTableNames")
            .done(function(data) {
                for (i = 0; i < data.length; i++) {
                    $('#tables').append($("<option></option>").text(data[i]))
                }
                $('#tables').material_select()
        })
}

function loadTable(data){
        $.get( "/loadTable", data)
            .done(function(data) {
                $('#table').bootstrapTable("destroy")
                $('#table').bootstrapTable({
                    columns: data.column,
                    data: data.data
                })
        })
}