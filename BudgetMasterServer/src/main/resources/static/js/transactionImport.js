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
        scrollY: false,
        columnDefs: [
            { orderable: false, targets:  5}
        ],
        language: { search: '' , searchPlaceholder: localizedSearch},
    });

    if(transactionNameSuggestions !== undefined)
    {
        let nameElements = document.querySelectorAll('.autocomplete');
        let autoCompleteInstances = M.Autocomplete.init(nameElements, {
            data: transactionNameSuggestions,
        });

        // prevent tab traversal for dropdown (otherwise "tab" needs to be hit twice to jump from name input to amount input)
        for(let i = 0; i < autoCompleteInstances.length; i++)
        {
            autoCompleteInstances[i].dropdown.dropdownEl.tabIndex = -1;
        }
    }
});