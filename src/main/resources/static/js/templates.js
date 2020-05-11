$(document).ready(function()
{
    if($('#modalConfirmDelete').length)
    {
        $('#modalConfirmDelete').modal('open');
    }

    if($('#buttonSaveAsTemplate').length)
    {
        $('#buttonSaveAsTemplate').click(function()
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
                url: $('#buttonSaveAsTemplate').attr('data-url'),
                data: {},
                success: function(data)
                {
                    createAndOpenModal(data)
                }
            });
        });
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
});

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