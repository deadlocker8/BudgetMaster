$(document).ready(function()
{
    M.FloatingActionButton.init(document.querySelectorAll('.edit-transaction-button'), {
        direction: 'left',
        hoverEnabled: false
    });

    $('.collapsible').collapsible();

    DataTable.type('date', 'className', 'dt-center');
    $('#table-transaction-rows').DataTable({
        paging: false,
        order: [[1, 'desc']],
        info: false,
        scrollX: true,
        scrollY: false,
        columnDefs: [
            {
                className: "dt-head-center",
                targets: [0, 1, 3, 4, 6]
            },
            {
                // amount column
                orderable: false,
                targets: 5
            },
            {
                width: '8%',
                targets: 5
            },
            {
                width: '10%',
                targets: [0, 1, 2, 6]
            },
            {
                width: '25%',
                targets: [3, 4]
            }
        ],
        language: {search: '', searchPlaceholder: localizedSearch},
    });

    if(transactionNameSuggestions !== undefined)
    {
        let nameElements = document.querySelectorAll('.autocomplete');
        let autoCompleteInstances = M.Autocomplete.init(nameElements, {
            data: transactionNameSuggestions,
            sortFunction: function(a, b, inputString)
            {
                return false;
            }
        });

        // prevent tab traversal for dropdown (otherwise "tab" needs to be hit twice to jump from name input to amount input)
        for(let i = 0; i < autoCompleteInstances.length; i++)
        {
            autoCompleteInstances[i].dropdown.dropdownEl.tabIndex = -1;
        }
    }

    initCsvTransactions();
});

function initCsvTransactions()
{
    initCsvTransactionForms();
    initCsvTransactionButtons();

    initCustomSelects();

    M.FloatingActionButton.init(document.querySelectorAll('.edit-transaction-button'), {
        direction: 'left',
        hoverEnabled: false
    });
}

function initCsvTransactionForms()
{
    const forms = document.querySelectorAll('[name="NewTransactionInPlace"]');

    for(let i = 0; i < forms.length; i++)
    {
        let form = forms[i];
        form.removeEventListener('submit', submitTransactionInPlaceForm);
        form.addEventListener('submit', submitTransactionInPlaceForm);
    }
}

function submitTransactionInPlaceForm(event)
{
    const form = event.target;
    const csvTransactionId = form.dataset.index;

    $.ajax({
        type: 'POST',
        url: $(this).attr('action'),
        data: new FormData(form),
        processData: false,
        contentType: false,
        success: function(response)
        {
            $('#transaction-import-row-' + csvTransactionId).replaceWith(response);
            initCsvTransactions();
        },
        error: function(response)
        {
            M.toast({
                html: "Error saving transaction",
                classes: 'red'
            });
            console.error(response);
        }
    });

    event.preventDefault();
}

function initCsvTransactionButtons()
{
    const buttonsSkip = document.getElementsByClassName('button-request-transaction-import-skip');
    for(let i = 0; i < buttonsSkip.length; i++)
    {
        const button = buttonsSkip[i];
        button.removeEventListener('click', skipRow);
        button.addEventListener('click', skipRow);
    }

    const buttonsUndoSkip = document.getElementsByClassName('button-request-transaction-import-undo-skip');
    for(let i = 0; i < buttonsUndoSkip.length; i++)
    {
        const button = buttonsUndoSkip[i];
        button.removeEventListener('click', undoSkipRow);
        button.addEventListener('click', undoSkipRow);
    }
}

function skipRow(event)
{
    performCsvTransactionGetRequestWithoutReload(event.currentTarget, 'Error skipping transaction');
}

function undoSkipRow(event)
{
    performCsvTransactionGetRequestWithoutReload(event.currentTarget, 'Error undo skip transaction');
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
            initCsvTransactions();
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
