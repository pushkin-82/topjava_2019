// $(document).ready(function () {
    $(function () {
        makeEditable({
            ajaxUrl: "ajax/profile/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "date_time"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    0,
                    "desc"
                ]
            })
        });
    });

function resetFilter() {
    $('#filtertable').get(0).reset();
}

function filter() {
    $.ajax({
        url: context.ajaxUrl + "filter",
        type: "GET",
        data: $("#filtertable").serialize()
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
        successNoty("Filtered")
    })
}
