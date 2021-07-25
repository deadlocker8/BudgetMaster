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

    $('.button-request-delete-category').click(function()
    {
        fetchAndShowModalContent(this.dataset.url, '#deleteModalContainerOnDemand', '#modalConfirmDelete', function(){
            initCustomSelects();

            $('#buttonDeleteCategory').click(function()
            {
                document.getElementById("formDestinationCategory").submit();
            });
         });
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
