$(document).ready(function()
{
    $('#modalConfirmDelete').modal('open');

    if($('#category-name').length)
    {
        document.getElementById('category-name').focus();
    }

    $('.category-color').click(function()
    {
        removeActive();
        addClass($(this)[0], "category-color-active");
        document.getElementById("categoryColor").value = rgb2hex($(this)[0].style.backgroundColor);
    });

    if($("#customColorPicker").length)
    {
        $("#customColorPicker").spectrum({
            showInitial: true,
            showInput: true,
            showButtons: false,
            preferredFormat: "hex",
            hide: function(color)
            {
                removeActive();
                addClass(document.getElementById("customColorPickerContainer"), "category-color-active");
                document.getElementById("customColorPickerContainer").style.backgroundColor = color.toHexString();
                document.getElementById("categoryColor").value = color.toHexString();
            },
            move: function(color)
            {
                document.getElementById("customColorPickerContainer").style.backgroundColor = color.toHexString();
            }
        });
    }

    $('#buttonDeleteCategory').click(function()
    {
        document.getElementById("formDestinationCategory").submit();
    });

    $('#button-remove-category-icon').click(function()
    {
        document.getElementById("category-icon-preview-icon").classList.toggle('hidden', true);
        document.getElementById("category-icon-placeholder").classList.toggle('hidden', false);
        document.getElementById("hidden-input-category-icon").value = '';
    });

    $('#button-category-icon-confirm').click(function()
    {
        let icon = document.querySelector('.category-icon-option.selected .category-icon-option-name').textContent;

        let previewIcon = document.getElementById("category-icon-preview-icon");
        previewIcon.className = '';  // clear class list

        icon.split(' ').forEach(function(cssClass) {
            previewIcon.classList.add(cssClass);
        });

        document.getElementById("category-icon-placeholder").classList.toggle('hidden', true);
        document.getElementById("hidden-input-category-icon").value = icon;
    });

    $('.category-icon-option').click(function()
    {
        let allIconOptions = document.querySelectorAll('.category-icon-option');
        for(let i = 0; i < allIconOptions.length; i++)
        {
            allIconOptions[i].classList.remove('selected');
        }

        this.classList.add('selected');
    });
});

function removeActive()
{
    let colors = document.getElementsByClassName("category-color");
    for(let i = 0; i < colors.length; i++)
    {
        removeClass(colors[i], "category-color-active");
    }
}