$(document).ready(function()
{
    if($("#fontColorPicker").length)
    {
        let fontColorPickerParent = document.getElementById('fontColorPicker');

        let fontColorPicker = new Picker({
            parent: fontColorPickerParent,
            popup: false,
            alpha: true,
            editor: true,
            editorFormat: 'hex',
            cancelButton: false,
            color: fontColorPickerParent.style.backgroundColor,
            onChange: function(color) {
                updateFontColor(fontColorPickerParent, color);
            },
            onClose: function(color) {
                updateFontColor(fontColorPickerParent, color);
            }
        });

        fontColorPicker.setColor(fontColorPickerParent.style.backgroundColor, true);
    }
});

function updateFontColor(parent, color)
{
    parent.style.backgroundColor = color.hex;
    document.getElementById("fontColor").value = color.hex;
}
