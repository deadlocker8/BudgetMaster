$(document).ready(function()
{
    M.FloatingActionButton.init(document.querySelectorAll('.edit-transaction-button'), {
        direction: 'left',
        hoverEnabled: false
    });

    $('.collapsible').collapsible();

    $('#table-transaction-rows').DataTable({
        paging: false,
        order: [[1, 'desc']],
        info: false,
        scrollX: true,
        scrollY: true,
        columnDefs: [
            { width: '30%', targets: 2},
            { width: '30%', targets: 3},
            { orderable: false, targets:  5}
        ],
        language: { search: '' , searchPlaceholder: localizedSearch},
    });
});