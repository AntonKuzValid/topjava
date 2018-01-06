var ajaxUrl = "ajax/meals/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#meals-datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
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
            [
                0,
                "desk"
            ]
        ]
    });
    makeEditable();

    $("#filter-form").submit(function () {
        filter();
        return false;
    });
});

function filter() {
    var form = $("#filter-form");
    $.get(ajaxUrl + "filter?" + form.serialize(), function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}

function resetMealForm() {
    $("#filter-form")[0].reset();
    updateTable();
}