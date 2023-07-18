$(document).ready(function()
{
    M.FloatingActionButton.init(document.querySelectorAll('.edit-transaction-button'), {
        direction: 'left',
        hoverEnabled: false
    });

    $('.collapsible').collapsible();

    $('#table-transaction-rows').DataTable({
        paging: false,
        order: [[2, 'desc']],
        info: false,
        scrollX: true,
        scrollY: false,
        columnDefs: [
            { orderable: false, targets:  6}
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
        console.log(i)
        console.log(form)
        // form.removeEventListener('submit', submitTransactionInPlaceForm);
        form.addEventListener('submit', submitTransactionInPlaceForm);
    }
}

function submitTransactionInPlaceForm(event)
{
    const form = event.target;
    console.log('form ' + form)
    const csvTransactionId = form.dataset.index;
            console.log('go')

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
