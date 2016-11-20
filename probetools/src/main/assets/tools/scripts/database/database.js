function loadTableNames(){
        $.get( "/loadTableNames")
            .done(function(data) {
                preloadOff()
                for (i = 0; i < data.length; i++) {
                    $('#tables').append($("<option></option>").text(data[i]))
                }
                $('#tables').material_select()
        })
}

function loadTable(data){
        if (data.table == 'Sql'){
            $('#table').bootstrapTable("destroy")
            preloadOff()
        } else{
            $.get( "/loadTable", data)
                    .done(function(data) {
                        preloadOff()
                        $('#table').bootstrapTable("destroy")
                        $('#table').bootstrapTable({
                            columns: data.column,
                            data: data.data
                        })
             })
        }
}

function runSQL(data){
        $.post( "/runSQL", data)
            .done(function(data) {
                $('#tables').val('Sql')
                $('#tables').material_select();

                preloadOff()
                $('#table').bootstrapTable("destroy")
                $('#table').bootstrapTable({
                     columns: data.column,
                     data: data.data
                })

               Materialize.toast('Success', 3000)
            })
            .fail(function(){
                preloadOff()
                Materialize.toast('Error. Bad sql', 3000)
            });
}