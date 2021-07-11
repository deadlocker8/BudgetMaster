$(document).ready(function()
{
    $('.button-remove-icon-from-item').click(function()
    {
        removeIcon();
    });

    $('#button-icon-confirm').click(function()
    {
        let activeTabName = document.querySelector('#iconTabs .tab a.active').dataset.name;
        if(activeTabName === 'images')
        {
            confirmImageIcon();
        }
        else
        {
            confirmBuiltinIcon();
        }
    });

    $('#item-icon-preview').click(function()
    {
        let modalID = '#modalIconSelect';
        let modalIconSelect = document.getElementById('modalIconSelect');
        let idToFocusOnClose = modalIconSelect.dataset.focusOnClose;

        $(modalID).modal({
            onOpenEnd: function f()
            {
                M.Tabs.init(document.querySelector('#iconTabs'), {
                    onShow: function()
                    {
                        deselectAll();
                    }
                });
            },
            onCloseEnd: function f()
            {
                document.getElementById(idToFocusOnClose).focus();
            }
        });
        $(modalID).modal('open');

        getAvailableImages();
    });

    $('#button-upload-new-image').click(function()
    {
        uploadImage();
    });

    if($('#modalIconSelect').length)
    {
        // prevent modal closing if "ENTER" is pressed in search
        let modalIconSelect = document.getElementById('modalIconSelect');
        modalIconSelect.addEventListener('keypress', function(event)
        {
            if(event.key === 'Enter')
            {
                event.preventDefault();
            }
        });
    }

    // select a built-in icon option
    $('.builtin-icon-option').click(function()
    {
        selectIcon(this, '.builtin-icon-option');
    });
});

function getAvailableImages()
{
    let progressIndicator = document.getElementById('progressIndicator');
    progressIndicator.style.setProperty('display', '', 'important')

    let availableImages = document.getElementById('available-images');
    availableImages.style.setProperty('display', 'none', 'important')

    $.ajax({
        type: 'GET',
        url: $('#available-images').attr('data-url'),
        data: {},
        success: function(data)
        {
            $('#available-images').html(data);

            // select an icon option
            $('.item-icon-option').click(function()
            {
                selectIcon(this, '.item-icon-option');
            });

            let classDeleteConfirm = 'item-icon-option-delete-confirm';
            $('.item-icon-option-delete').click(function()
            {
                if(this.classList.contains(classDeleteConfirm))
                {
                    deleteImage(this);
                    removeIcon();
                }
                else
                {
                    this.classList.add(classDeleteConfirm);
                }
            });

            progressIndicator.style.setProperty('display', 'none', 'important')
            availableImages.style.setProperty('display', '', 'important')
        }
    });
}

function deselectAll()
{
    let allIconOptions = document.querySelectorAll('.builtin-icon-option, .item-icon-option');
    for(let i = 0; i < allIconOptions.length; i++)
    {
        allIconOptions[i].classList.remove('selected');
    }

    document.getElementById('button-icon-confirm').setAttribute('disabled', 'true');
}

function selectIcon(item)
{
    deselectAll();

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

            getAvailableImages();
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
            let isDeleteSuccessful = parsedData['isDeleteSuccessful']
            M.toast({
                html: parsedData['localizedMessage'],
                classes: isDeleteSuccessful ? 'green' : 'red'
            });

            getAvailableImages();
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

function confirmImageIcon()
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
    document.getElementById("builtin-icon-preview-icon").classList.toggle('hidden', true);
    document.getElementById("item-icon-placeholder").classList.toggle('hidden', true);
    document.getElementById("hidden-input-icon-image-id").value = iconId;
    document.getElementById("hidden-input-icon-builtin-identifier").value = null;
}

function confirmBuiltinIcon()
{
    let icon = document.querySelector('.builtin-icon-option.selected .builtin-icon-option-name');
    if(icon === null)
    {
        return false;
    }

    let iconIdentifier = icon.textContent;

    let previewIcon = document.getElementById("builtin-icon-preview-icon");
    previewIcon.className = '';  // clear class list

    iconIdentifier.split(' ').forEach(function(cssClass)
    {
        previewIcon.classList.add(cssClass);
    });

    document.getElementById("item-icon-preview-icon").classList.toggle('hidden', true);
    document.getElementById("builtin-icon-preview-icon").classList.toggle('hidden', false);
    document.getElementById("item-icon-placeholder").classList.toggle('hidden', true);
    document.getElementById("hidden-input-icon-image-id").value = null;
    document.getElementById("hidden-input-icon-builtin-identifier").value = iconIdentifier;
}

function searchBuiltinIcons()
{
    let searchWord = document.getElementById('searchIcons').value.toLowerCase();

    let allIcons = document.querySelectorAll('.builtin-icon-option-column');
    let numberOfMatchingIcons = 0;
    for(let i = 0; i < allIcons.length; i++)
    {
        let iconName = allIcons[i].querySelector('.builtin-icon-option-name').textContent;
        if(iconName.toLowerCase().includes(searchWord))
        {
            allIcons[i].classList.toggle('hidden', false);
            numberOfMatchingIcons++;
        }
        else
        {
            allIcons[i].classList.toggle('hidden', true);
        }
    }

    document.getElementById('numberOfMatchingIcons').innerText = numberOfMatchingIcons.toString();
}

function removeIcon()
{
    document.getElementById("item-icon-preview-icon").classList.toggle('hidden', true);
    document.getElementById("item-icon-placeholder").classList.toggle('hidden', false);
    document.getElementById("hidden-input-icon-image-id").value = null;
    document.getElementById("builtin-icon-preview-icon").classList.toggle('hidden', true);
    document.getElementById("hidden-input-icon-builtin-identifier").value = null;
}