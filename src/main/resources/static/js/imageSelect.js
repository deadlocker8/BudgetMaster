$(document).ready(function()
{
    $('.button-remove-icon-from-item').click(function()
    {
        document.getElementById("item-icon-preview-icon").classList.toggle('hidden', true);
        document.getElementById("item-icon-placeholder").classList.toggle('hidden', false);
        document.getElementById("hidden-input-icon").value = '';
    });

    $('#button-icon-confirm').click(function()
    {
        let icon = document.querySelector('.item-icon-option.selected .item-icon-preview');
        if(icon === null)
        {
            return false;
        }

        let iconPath = icon.src;
        let iconId = icon.dataset.imageId;

        let previewIcon = document.getElementById("item-icon-preview-icon");
        previewIcon.src = iconPath;

        document.getElementById("item-icon-preview-icon").classList.toggle('hidden', false);
        document.getElementById("item-icon-placeholder").classList.toggle('hidden', true);
        document.getElementById("hidden-input-icon").value = iconId;
    });

    $('#item-icon-preview').click(function()
    {
        getAvailableImages(function()
        {
            let modalID = '#modalIconSelect';
            let modalIconSelect = document.getElementById('modalIconSelect');
            let idToFocusOnClose = modalIconSelect.dataset.focusOnClose;

            $(modalID).modal({
                onCloseEnd: function f()
                {
                    document.getElementById(idToFocusOnClose).focus();
                }
            });
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
        url: $('#item-icon-preview').attr('data-url'),
        data: {},
        success: function(data)
        {
            $('#available-images').html(data);

            // select an icon option
            $('.item-icon-option').click(function()
            {
                selectIcon(this);
            });

            let classDeleteConfirm = 'item-icon-option-delete-confirm';
            $('.item-icon-option-delete').click(function()
            {
                if(this.classList.contains(classDeleteConfirm))
                {
                    deleteImage(this);
                }
                else
                {
                    this.classList.add(classDeleteConfirm);
                }
            });

            callback();
        }
    });
}

function selectIcon(item)
{
    let allIconOptions = document.querySelectorAll('.item-icon-option');
    for(let i = 0; i < allIconOptions.length; i++)
    {
        allIconOptions[i].classList.remove('selected');
    }

    item.classList.add('selected');
    document.getElementById('button-icon-confirm').removeAttribute('disabled');
}

function uploadImage()
{
    let formID = 'form-upload-image';
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