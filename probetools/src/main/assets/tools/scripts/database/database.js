function loadTableNames(){
        $.get( "/loadTableNames")
            .done(function(data) {
                preloadOff()
                for (i = 0; i < data.length; i++) {
                    $('#tables').append($("<option></option>").text(data[i]))
                    $('#dropdown_tables').append(
                        $('<li>').attr('id', data[i]).append($('<span>').attr('class', 'tab').append(data[i])));
                        $('#'+data[i]).on("click", function(event){
                            $('.brand-logo').html('<i data-activates="slide-out" class="material-icons button-collapse">menu</i>' + this.id);
                            $(".button-collapse").sideNav();
                            var data = {};
                            preload0n();
                            data.table = this.id;
                            loadTable(data);
                        });
                }
                $('#tables').material_select()
                $(".dropdown-button").dropdown({
                   constrain_width: false,
                   belowOrigin: true,
                })
             })
            .fail(function(){
                preloadOff()
            });
}

function loadTable(data){
        if (data.table == 'Sql'){
            preloadOff()
        } else{
            $.get( "/loadTable", data)
                    .done(function(data) {
                        preloadOff()
                        datatable.destroy();
                        $('#tabledb').empty();
                        datatable = $('#tabledb').DataTable({
                            "scrollY": 500,
                            "scrollX": true,
                            "lengthChange": false,
                            data: data.data,
                            columns: data.column
                        });
                    })
                    .fail(function(){
                        preloadOff()
                    });
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