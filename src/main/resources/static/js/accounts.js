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
        getAvailableImages(function()
        {
            let modalID = '#modalAccountIconSelect';
            $(modalID).modal();
            $(modalID).modal('open');
        });
    });

    $('#button-upload-new-image').click(function()
    {
        uploadImage();
    });
});

function getAvailableImages(callback)
{
    $.ajax({
        type: 'GET',
        url: $('#account-icon-preview').attr('data-url'),
        data: {},
        success: function(data)
        {
            $('#available-images').html(data);

            // select an icon option
            $('.account-icon-option').click(function()
            {
                selectIcon(this);
            });

            $('.account-icon-option-delete').click(function()
            {
                deleteImage(this);
            });

            callback();
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

function uploadImage()
{
    let formID = 'form-upload-account-image';
    let form = document.getElementById(formID);

    $.ajax({
        url: form.action,
        enctype: 'multipart/form-data',
        type: 'post',
        processData: false,
        contentType: false,
        cache: false,
        data: new FormData(form),
        success: function(response)
        {
            let parsedData = JSON.parse(response);
            let isUploadSuccessful = parsedData['isUploadSuccessful']
            M.toast({
                html: parsedData['localizedMessage'],
                classes: isUploadSuccessful ? 'green' : 'red'
            });

            getAvailableImages(function()
            {
            });
        },
        error: function(response)
        {
            let parsedData = JSON.parse(response);
            M.toast({
                html: parsedData['localizedMessage'],
                classes: 'red'
            });
        }
    });
}

function deleteImage(item)
{
    $.ajax({
        type: 'GET',
        url: $(item).attr('data-url'),
        data: {},
        success: function(response)
        {
            let parsedData = JSON.parse(response);
            let isUploadSuccessful = parsedData['isDeleteSuccessful']
            M.toast({
                html: parsedData['localizedMessage'],
                classes: isUploadSuccessful ? 'green' : 'red'
            });

            getAvailableImages(function()
            {
            });
        },
        error: function(response)
        {
            let parsedData = JSON.parse(response);
            M.toast({
                html: parsedData['localizedMessage'],
                classes: 'red'
            });
        }
    });
}