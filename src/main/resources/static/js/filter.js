$(document).ready(function()
{
    $('.collapsible').collapsible();

    $(':checkbox').change(function()
    {
        updateStatus();
    });

    $('#filter-name').on('input', function()
    {
        updateStatus();
    });

    $('#section-categories .filter-button-all').click(function()
    {
        setAll('section-categories', true);
        updateStatus();
    });

    $('#section-categories .filter-button-none').click(function()
    {
        setAll('section-categories', false);
        updateStatus();
    });

    $('#section-tags .filter-button-all').click(function()
    {
        setAll('section-tags', true);
        updateStatus();
    });

    $('#section-tags .filter-button-none').click(function()
    {
        setAll('section-tags', false);
        updateStatus();
    });

    updateStatus();
});

function updateStatus()
{
    updateStatusForSectionByCheckboxes('section-type');
    updateStatusForSectionByCheckboxes('section-repeating');
    updateStatusForSectionByCheckboxes('section-categories');
    updateStatusForSectionByCheckboxes('section-tags');
    updateStatusForSectionName('section-name');
}

function updateStatusForSectionByCheckboxes(identifier)
{
    let section = document.getElementById(identifier);
    section.querySelector('.collapsible-header-status').innerText = getStatusByCheckboxes(section);
}

function getStatusByCheckboxes(item)
{
    let checkboxes = $(item).find('input[type=checkbox]');
    let checkedCount = 0;
    for(let i = 0; i < checkboxes.length; i++)
    {
        if(checkboxes[i].checked)
        {
            checkedCount += 1;
        }
    }

    return checkedCount + "/" + checkboxes.length;
}

function updateStatusForSectionName(identifier)
{
    let section = document.getElementById(identifier);
    let nameValue = section.querySelector('#filter-name').value;
    let statusText = '1/1';

    if(nameValue.length === 0)
    {
        statusText = '0/1';
    }
    section.querySelector('.collapsible-header-status').innerText = statusText;
}

function setAll(identifier, checked)
{
    let section = document.getElementById(identifier);
    let checkboxes = $(section).find('input[type=checkbox]');
    for(let i = 0; i < checkboxes.length; i++)
    {
        checkboxes[i].checked = checked;
    }
}