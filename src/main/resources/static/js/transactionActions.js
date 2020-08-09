$(document).ready(function()
{
    M.FloatingActionButton.init(document.querySelectorAll('#transaction-actions-button'), {});

    $('.transaction-action').click(function()
    {
        let actionType = $(this).attr('data-action-type');
        if(actionType === 'saveAsTemplate')
        {
            openSaveAsTemplateModal(this);
        }
        else if(actionType === 'changeType')
        {
            openChangeTransactionTypeModal(this);
        }
    });
});

function openSaveAsTemplateModal(item)
{
    // check if transaction form is valid
    let isValidForm = validateForm(true);
    if(!isValidForm)
    {
        $('#modalCreateFromTransaction').modal('close');
        M.toast({html: createTemplateWithErrorInForm});
        return;
    }

    $.ajax({
        type: 'GET',
        url: $(item).attr('data-url'),
        data: {},
        success: function(data)
        {
            createAndOpenModal(data)
        }
    });
}

function createAndOpenModal(data)
{
    let modalID = '#modalCreateFromTransaction';

    $('#saveAsTemplateModalContainer').html(data);
    $(modalID).modal();
    $(modalID).modal('open');
    let templateNameInput = document.getElementById('template-name');
    templateNameInput.focus();
    $(templateNameInput).on('keypress', function(e)
    {
        let code = e.keyCode || e.which;
        if(code === 13)
        {
            saveAsTemplate();
        }
    });

    $('#buttonCreateTemplate').click(function()
    {
        saveAsTemplate();
    });
}

function saveAsTemplate()
{
    // validate template name
    let templateName = document.getElementById('template-name').value;
    let isValid = validateTemplateName(templateName);
    if(!isValid)
    {
        return
    }

    let form = document.getElementsByName('NewTransaction')[0];
    form.appendChild(createAdditionalHiddenInput('templateName', templateName));
    form.appendChild(createAdditionalHiddenInput('includeCategory', document.getElementById('include-category').checked));
    form.appendChild(createAdditionalHiddenInput('includeAccount', document.getElementById('include-account').checked));

    // replace form target url
    form.action = $('#buttonCreateTemplate').attr('data-url');
    form.submit();
}

function validateTemplateName(templateName)
{
    if(templateName.length === 0)
    {
        addTooltip('template-name', templateNameEmptyValidationMessage);
        return false;
    }
    else
    {
        removeTooltip('template-name');
    }

    if(existingTemplateNames.includes(templateName))
    {
        addTooltip('template-name', templateNameDuplicateValidationMessage);
        return false;
    }
    else
    {
        removeTooltip('template-name');
    }

    return true;
}

function createAdditionalHiddenInput(name, value)
{
    let newInput = document.createElement('input');
    newInput.setAttribute('type', 'hidden');
    newInput.setAttribute('name', name);
    newInput.setAttribute('value', value);
    return newInput;
}

function openChangeTransactionTypeModal(item)
{
    console.log($(item).attr('data-url'));
    $.ajax({
        type: 'GET',
        url: $(item).attr('data-url'),
        data: {},
        success: function(data)
        {
            createAndOpenModalSelectNewType(data)
        }
    });
}

function createAndOpenModalSelectNewType(data)
{
    let modalID = '#modalChangeTransactionType';

    $('#changeTransactionTypeModalContainer').html(data);
    $(modalID).modal();
    $(modalID).modal('open');
    $('#newTypeSelect').formSelect();

    $('#buttonChangeTransactionType').click(function()
    {
        let newType = document.getElementById('newTypeSelect').value;
        document.getElementById('inputNewType').setAttribute('value', newType);

        let form = document.getElementById('formChangeTransactionType');
        form.submit();
    });
}