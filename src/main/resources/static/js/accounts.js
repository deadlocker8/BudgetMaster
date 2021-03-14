$(document).ready(function()
{
    if($('#modalConfirmDelete').length)
    {
        $('#modalConfirmDelete').modal('open');
    }

    if($('#modalAccountNotDeletable').length)
    {
        $('#modalAccountNotDeletable').modal('open');
    }

    if($('#account-name').length)
    {
        document.getElementById('account-name').focus();
    }

    $('#button-remove-account-icon').click(function()
    {
        document.getElementById("account-icon-preview-icon").classList.toggle('hidden', true);
        document.getElementById("account-icon-placeholder").classList.toggle('hidden', false);
        document.getElementById("hidden-input-account-icon").value = '';
    });

    $('#button-account-icon-confirm').click(function()
    {
        let icon = document.querySelector('.account-icon-option.selected .account-icon-preview');
        if(icon === null)
        {
            return false;
        }

        let iconPath = icon.src;
        let iconId = icon.dataset.imageId;

        let previewIcon = document.getElementById("account-icon-preview-icon");
        previewIcon.src = iconPath;

        document.getElementById("account-icon-preview-icon").classList.toggle('hidden', false);
        document.getElementById("account-icon-placeholder").classList.toggle('hidden', true);
        document.getElementById("hidden-input-account-icon").value = iconId;
    });

    // select an icon option
    $('.account-icon-option').click(function()
    {
        let allIconOptions = document.querySelectorAll('.account-icon-option');
        for(let i = 0; i < allIconOptions.length; i++)
        {
            allIconOptions[i].classList.remove('selected');
        }

        this.classList.add('selected');
    });

    if($('#modalAccountIconSelect').length)
    {
        let modalAccountIconSelect = document.getElementById('modalAccountIconSelect');
        M.Modal.init(modalAccountIconSelect, {
            onCloseEnd: function f()
            {
                document.getElementById('account-name').focus();
            }
        });
    }
});