function initSettingsContainer(formName, containerId)
{
    $("[name='" + formName + "']").submit(function(event)
    {
        let form = document.getElementsByName(formName)[0];

        $.ajax({
            type: 'POST',
            url: $(this).attr('action'),
            data: $(this).serialize(),
            processData: false,
            success: function(response)
            {
                M.Toast.dismissAll();

                $('#' + containerId).html(response);

                // re-init materialize components
                $('.tooltipped').tooltip();
                $('select').formSelect();

                toggleSettingsContainerHeader(containerId + 'Header', true);

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
