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
            onChange: function(color)
            {
                updateFontColor(fontColorPickerParent, color);
            },
            onClose: function(color)
            {
                updateFontColor(fontColorPickerParent, color);
            }
        });

        fontColorPicker.setColor(fontColorPickerParent.style.backgroundColor, true);

        $('#buttonFontColorAuto').click(function()
        {
            document.getElementById("fontColor").value = null;

            let backgroundColor = document.getElementById('item-icon-preview-background').style.backgroundColor;

            let appropriateColor;
            try
            {
                let rgb = extractRGB(backgroundColor);
                appropriateColor = getAppropriateTextColor(rgb);
            }
            catch(e)
            {
                appropriateColor = '#2E79B9';
            }

            document.getElementById("item-icon-preview").style.color = appropriateColor;

            fontColorPicker.setColor(appropriateColor, true);
        });
    }
});

function updateFontColor(parent, color)
{
    parent.style.backgroundColor = color.hex;
    document.getElementById("fontColor").value = color.hex;
    document.getElementById("item-icon-preview").style.color = color.hex;
}

function extractRGB(rgbaString)
{
    return rgbaString.match(/^rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,\s*(\d+\.{0,1}\d*))?\)$/).slice(1, 4);
}

function getAppropriateTextColor(colorRGB)
{
    // Counting the perceptive luminance - human eye favors green color...
    let a = 1 - (0.299 * colorRGB[0] + 0.587 * colorRGB[1] + 0.114 * colorRGB[2]) / 255;
    if(a < 0.5)
    {
        return '#000000';
    }
    else
    {
        return '#FFFFFF'
    }
}
