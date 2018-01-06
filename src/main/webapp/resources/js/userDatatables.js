var ajaxUrl = "ajax/admin/users/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
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
    });
    makeEditable();
});

// Browser don't find this function
function enableUser(id) {
    var isChecked = $("#" + id).find("input").prop("checked");
    $.post(ajaxUrl + "enable",
        {
            id: id,
            enabled: isChecked
        },
        function () {
            $("#" + id).css("color", isChecked ? "black" : "lightgrey");
            $("#" + id).find("input").prop("checked", isChecked);
            successNoty("Saved");
        }
    );
}