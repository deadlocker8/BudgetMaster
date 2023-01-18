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
            { orderable: false, targets:  5}
        ],
        language: { search: '' , searchPlaceholder: localizedSearch},
    });
});