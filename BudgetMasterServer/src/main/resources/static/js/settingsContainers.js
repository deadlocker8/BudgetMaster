function initSettingsContainer(formName, containerId)
{
    $("[name='" + formName + "']").submit(function(event)
    {
        let form = document.getElementsByName(formName)[0];

        $.ajax({
            type: 'POST',
            url: form.action.formAction,
            data: new FormData(form),
            processData: false,
            contentType: false,
            success: function(response)
            {
                M.Toast.dismissAll();

                $('#' + containerId).html(response);

                // re-init materialize components
                $('.tooltipped').tooltip();
                $('select').formSelect();

                let toastContent = document.querySelector('#' + containerId + ' .securityContainerToastContent').innerHTML.trim();
                if(toastContent)
                {
                    let data = JSON.parse(toastContent);
                    M.toast({
                        html: data['localizedMessage'],
                        classes: data['classes']
                    });
                }
            },
            error: function(response)
            {
                M.toast({
                    html: "Error saving settings",
                    classes: 'red'
                });
                console.error(response);
            }
        });

        event.preventDefault();
    });
}
