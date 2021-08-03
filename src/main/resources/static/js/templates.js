$(document).ready(function()
{
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
        handleIncludeAccountCheckbox('include-transfer-account', 'transaction-destination-account')
    }

    if($("#searchTemplate").length)
    {
        document.getElementById('searchTemplate').focus();
        enableTemplateHotKeys();
    }

    $('.button-request-delete-template').click(function()
    {
        fetchAndShowModalContent(this.dataset.url, '#deleteModalContainerOnDemand', '#modalConfirmDelete', function(){});
    });
});

let selectedTemplateName = null;

function handleIncludeAccountCheckbox(checkboxID, selectID)
{
    document.getElementById(checkboxID).addEventListener('change', (event) =>
    {
        let accountSelect = document.getElementById(selectID);
        accountSelect.classList.toggle("disabled", !event.target.checked);
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

        // hide all item selections
        let templateItems = document.getElementsByClassName('template-item');
        for(let i = 0; i < templateItems.length; i++)
        {
            toggleItemSelection(templateItems[i], false);
        }
        selectedTemplateName = null;
    }
    else
    {
        collapsible.classList.remove('hidden');
    }

    handleKeyUpOrDown(null);
}

function enableTemplateHotKeys()
{
    Mousetrap.bind('up', function()
    {
        handleKeyUpOrDown(true);
    });

    Mousetrap.bind('down', function()
    {
        handleKeyUpOrDown(false);
    });

    Mousetrap.bind('enter', function()
    {
        if(!isSearchFocused())
        {
            confirmTemplateSelection();
        }
    });

    handleKeyUpOrDown(false);
}

function handleKeyUpOrDown(isUp)
{
    let templateItems = document.getElementsByClassName('template-item');
    for(let i = 0; i < templateItems.length; i++)
    {
        toggleItemSelection(templateItems[i], false);
    }
    templateItems = document.querySelectorAll('.template-item:not(.hidden)');

    if(templateItems.length === 0)
    {
        selectedTemplateName = null;
        return;
    }

    let previousIndex = getIndexOfTemplateName(templateItems, selectedTemplateName);
    let noItemSelected = selectedTemplateName === null;
    let previousItemNoLongerInList = previousIndex === null;

    if(noItemSelected || previousItemNoLongerInList)
    {
        // select the first item
        selectItem(templateItems, 0);
    }
    else
    {
        if(isUp === null )
        {
            selectItem(templateItems, previousIndex);
            return;
        }

        // select next item
        if(isUp)
        {
            selectNextItemOnUp(templateItems, previousIndex);
        }
        else
        {
            selectNextItemOnDown(templateItems, previousIndex);
        }
    }
}

function selectItem(templateItems, index)
{
    toggleItemSelection(templateItems[index], true);
    selectedTemplateName = getTemplateName(templateItems[index]);
    document.getElementById('searchTemplate').focus();
}

function toggleItemSelection(templateItem, isSelected)
{
    templateItem.getElementsByClassName('collapsible-header')[0].classList.toggle('template-selected', isSelected);
}

function getTemplateName(templateItem)
{
    return templateItem.getElementsByClassName('template-header-name')[0];
}

function getIndexOfTemplateName(templateItems, templateName)
{
    for(let i = 0; i < templateItems.length; i++)
    {
        let currentTemplateName = getTemplateName(templateItems[i]);
        if(currentTemplateName === templateName)
        {
            return i;
        }
    }

    return null;
}

function selectNextItemOnDown(templateItems, previousIndex)
{
    let isLastItemSelected = previousIndex + 1 === templateItems.length;
    if(isLastItemSelected)
    {
        selectItem(templateItems, 0);
    }
    else
    {
        selectItem(templateItems, previousIndex + 1);
    }
}

function selectNextItemOnUp(templateItems, previousIndex)
{
    let isFirstItemSelected = previousIndex === 0;
    if(isFirstItemSelected)
    {
        selectItem(templateItems, templateItems.length - 1);
    }
    else
    {
        selectItem(templateItems, previousIndex - 1);
    }
}

function confirmTemplateSelection()
{
    let templateItems = document.querySelectorAll('.template-item:not(.hidden)');
    if(templateItems.length === 0)
    {
        selectedTemplateName = null;
        return;
    }

    let index = getIndexOfTemplateName(templateItems, selectedTemplateName);
    let indexItemNoLongerInList = index === null;
    let noItemSelected = selectedTemplateName === null;

    if(noItemSelected || indexItemNoLongerInList)
    {
        return;
    }

    templateItems[index].getElementsByClassName('button-select-template')[0].click();
}