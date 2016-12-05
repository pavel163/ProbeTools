function loadTableNames(){
        preloadOn();
        $.get( "/loadTableNames")
            .done(function(data) {
                for (i = 0; i < data.length; i++) {
                    $('#tables').append($("<option></option>").text(data[i]))
                    $('#dropdown_tables').append(
                        $('<li>').attr('id', data[i]).append($('<span>').attr('class', 'tab').append(data[i])));
                        $('#'+data[i]).on("click", function(event){
                            $('.brand-logo').html('<i data-activates="slide-out" class="material-icons button-collapse">menu</i>' + this.id);
                            $(".button-collapse").sideNav();
                            var data = {};
                            preloadOn();
                            data.table = this.id;
                            loadTable(data);
                        });
                }
                $('#tables').material_select()
                $(".dropdown-button").dropdown({
                   constrain_width: false,
                   belowOrigin: true,
                })
                preloadOff();
             })
            .fail(function(){
                preloadOff();
            });
}

function loadTable(data){
        preloadOn();
        $.get("/loadTable", data)
            .done(function(data) {
                createNewTable(data)
                preloadOff();
            })
            .fail(function(){
                preloadOff();
            });
}

function runSQL(data){
        preloadOn();
        $.post( "/runSQL", data)
            .done(function(data) {
                if (data.column != 0) {
                    createNewTable(data);
                } else {
                    var data = {};
                    data.table = $('.brand-logo').text().replace('menu', '');
                    loadTable(data);
                }
                preloadOff();
                Materialize.toast('Success', 3000);
            })
            .fail(function(){
                preloadOff();
                Materialize.toast('Error. Bad sql', 3000);
            });
}

function createNewTable(data){
    datatable.destroy();
    $('#tabledb').empty();
    datatable = $('#tabledb').DataTable({
        "scrollX": true,
        "lengthChange": false,
        data: data.data,
        columns: data.column
    });
}