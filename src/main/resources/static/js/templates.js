$(document).ready(function()
{
    if($('#modalConfirmDelete').length)
    {
        $('#modalConfirmDelete').modal('open');
    }

    M.Collapsible.init(document.querySelector('.collapsible.expandable'), {
        accordion: false
    });

    let inputSearchTemplate = document.getElementById('searchTemplate');
    if(inputSearchTemplate !== undefined)
    {
        $(inputSearchTemplate).on('change keydown paste input', function()
        {
            let searchText = $(this).val();
            searchTemplates(searchText);
        });
    }

    if($("#template-name").length)
    {
        document.getElementById('template-name').focus();
    }

    if($("#include-account").length)
    {
        handleIncludeAccountCheckbox('include-account', 'transaction-account')
    }

    if($("#include-transfer-account").length)
    {
        handleIncludeAccountCheckbox('include-transfer-account', 'transaction-transfer-account')
    }

    if($("#searchTemplate").length)
    {
        document.getElementById('searchTemplate').focus();
    }
});

function handleIncludeAccountCheckbox(checkboxID, selectID)
{
    document.getElementById(checkboxID).addEventListener('change', (event) =>
    {
        let accountSelect = document.getElementById(selectID)
        let accountSelectInstance = M.FormSelect.getInstance(accountSelect);
        accountSelectInstance.destroy();
        accountSelect.disabled = !event.target.checked;
        M.FormSelect.init(document.querySelectorAll('#' + selectID), {});
    });
}

function searchTemplates(searchText)
{
    searchText = searchText.trim();
    searchText = searchText.toLowerCase()

    let templateItems = document.querySelectorAll('.template-item');
    let collapsible = document.getElementById('templateCollapsible');

    if(!searchText)
    {
        templateItems.forEach((item) =>
        {
            collapsible.classList.remove('hidden');
            item.classList.remove('hidden');
        });
        return;
    }

    let numberOfVisibleItems = 0;
    for(let i = 0; i < templateItems.length; i++)
    {
        let item = templateItems[i];
        let templateName = item.querySelector('.template-header-name').innerText;
        if(templateName.toLowerCase().includes(searchText))
        {
            item.classList.remove('hidden');
            numberOfVisibleItems++;
        }
        else
        {
            item.classList.add('hidden');
        }
    }

    // hide whole collapsible to prevent shadows from remaining visible
    if(numberOfVisibleItems === 0)
    {
        collapsible.classList.add('hidden');
    }
    else
    {
        collapsible.classList.remove('hidden');
    }
}
