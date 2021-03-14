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

    $('#account-icon-preview').click(function()
    {
        openSelectAccountIconModal(this);
    });
});

function openSelectAccountIconModal(item)
{
    $.ajax({
        type: 'GET',
        url: $(item).attr('data-url'),
        data: {},
        success: function(data)
        {
            let modalID = '#modalAccountIconSelect';
            $('#available-images').html(data);

            // select an icon option
            $('.account-icon-option').click(function()
            {
               selectIcon(this);
            });

            $(modalID).modal();
            $(modalID).modal('open');
        }
    });
}

function selectIcon(item)
{
    let allIconOptions = document.querySelectorAll('.account-icon-option');
    for(let i = 0; i < allIconOptions.length; i++)
    {
        allIconOptions[i].classList.remove('selected');
    }

    item.classList.add('selected');
}