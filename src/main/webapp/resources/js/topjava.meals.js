let mealAjaxUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax" : {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 16).replace("T", " ");
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: function () {
            $.get(mealAjaxUrl, updateFilteredTable());
        }
    });


    $("#startDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $("#endDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $("#startTime").datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
    $("#endTime").datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
    $("#dateTime").datetimepicker({
        format: 'Y-m-d H:i'
    });
});

// $.ajaxSetup({
//     converters: {
//         "text json": function(string) {
//             let result = JSON.parse(string);
//             $(result).each(function () {
//                 this.dateTime = this.dateTime.substring(0, 16).replace("T", " ")
//             });
//             return result;
//         }
//     }
// });
