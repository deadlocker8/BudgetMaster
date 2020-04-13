$(document).ready(function()
{
    if($("#modalConfirmDelete").length)
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
                url: $("#buttonSaveAsTemplate").attr("data-url"),
                data: {},
                success: function(data)
                {
                    $('#saveAsTemplateModalContainer').html(data);
                    $('#modalCreateFromTransaction').modal();
                    $('#modalCreateFromTransaction').modal('open');
                    $('#buttonCreateTemplate').click(function(e)
                    {
                        // validate template name
                        let templateName = document.getElementById('template-name').value;
                        let isValid = validateTemplateName(templateName);
                        if(!isValid)
                        {
                            return
                        }

                        // insert additional input for template name
                        let inputTemplateName = document.createElement('input');
                        inputTemplateName.setAttribute('type', 'hidden');
                        inputTemplateName.setAttribute('name', 'templateName');
                        inputTemplateName.setAttribute('value', templateName);

                        let form = document.getElementsByName('NewTransaction')[0];
                        form.appendChild(inputTemplateName);

                        // replace form target url
                        form.action = $("#buttonCreateTemplate").attr("data-url");

                        form.submit();
                    });
                }
            });
        });
    }
});

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