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
    $.ajax({
        url: context.ajaxUrl,
        type: "GET",
        data: $("#datatable").serialize()
    }).done(function (data) {
        updateData(data);
        successNoty("Cancelled filter");
    })
}

function filter() {
    $.ajax({
        url: context.ajaxUrl + "filter",
        type: "GET",
        data: $("#filtertable").serialize()
    }).done(function (data) {
        updateData(data);
        successNoty("Filtered")
    })
}
