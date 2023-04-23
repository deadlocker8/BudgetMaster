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

    initCsvTransactionButtons();
});

function initCsvTransactionButtons()
{
    const buttonsSkip = document.getElementsByClassName('button-request-transaction-import-skip');
    for(let i = 0; i < buttonsSkip.length; i++)
    {
        const button = buttonsSkip[i];
        button.addEventListener('click', function()
        {
            performCsvTransactionGetRequestWithoutReload(button, 'Error skipping transaction');
        });
    }

    const buttonsUndoSkip = document.getElementsByClassName('button-request-transaction-import-undo-skip');
    for(let i = 0; i < buttonsUndoSkip.length; i++)
    {
        const button = buttonsUndoSkip[i];
        button.addEventListener('click', function()
        {
            performCsvTransactionGetRequestWithoutReload(button, 'Error undo skip transaction');
        });
    }
}

function performCsvTransactionGetRequestWithoutReload(button, errorMessage)
{
    const url = button.dataset.url;
    const csvTransactionId = button.dataset.index;

    $.ajax({
        type: 'GET',
        url: url,
        data: {},
        success: function(data)
        {
            $('#transaction-import-row-' + csvTransactionId).replaceWith(data);
            initCsvTransactionButtons();
        },
        error: function(response)
        {
            M.toast({
                html: errorMessage,
                classes: 'red'
            });
            console.error(response);
        }
    });
}
