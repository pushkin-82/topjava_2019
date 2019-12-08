// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );
});

function updateEnabled(enabled, id) {
    let isEnabled = enabled.is(":checked");
    $.ajax({
        url: context.ajaxUrl + id + "/enabled?",
        type: "POST",
        data: "enabled=" + isEnabled
    }).done(function () {
        enabled.closest("tr").attr("data-userEnabled", isEnabled);
        successNoty(isEnabled ? "Enabled user " + id : "Disabled user " + id);
    }).fail(function () {
        $(enabled).prop("checked", !isEnabled);
    })
}