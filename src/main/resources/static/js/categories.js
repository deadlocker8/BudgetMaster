$( document ).ready(function() {
    $('#modalConfirmDelete').modal('open');

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
            hide: function (color) {
                removeActive();
                addClass(document.getElementById("customColorPickerContainer"), "category-color-active");
                document.getElementById("customColorPickerContainer").style.backgroundColor = color.toHexString();
                document.getElementById("categoryColor").value = color.toHexString();
            },
            move: function (color) {
                document.getElementById("customColorPickerContainer").style.backgroundColor = color.toHexString();
            }
        });
    }
});

function removeActive()
{
    var colors = document.getElementsByClassName("category-color");
    for(var i=0; i < colors.length; i++)
    {
        removeClass(colors[i], "category-color-active");
    }
}