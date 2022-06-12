$("[name='SecuritySettingsContainer']").submit(function(event)
{
    let form = document.getElementsByName('SecuritySettingsContainer')[0];

    $.ajax({
        type: 'POST',
        url: form.action.formAction,
        data: new FormData(form),
        processData: false,
        contentType: false,
        success: function(response)
        {
            $('#securitySettingsContainer').html(response);
            $('.tooltipped').tooltip();

            let toastContent = document.querySelector('#securitySettingsContainer .securityContainerToastContent').innerHTML.trim();
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
                html: "Error saving security settings",
                classes: 'red'
            });
            console.error(response);
        }
    });

    event.preventDefault();
});
