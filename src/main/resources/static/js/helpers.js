NUMBER_REGEX = new RegExp("^\\d+$");

function validateNumber(text, ID, hiddenID, message)
{
    if(text.match(NUMBER_REGEX) == null)
    {
        addTooltip(ID, message);
        if(hiddenID != null)
        {
            document.getElementById(hiddenID).value = "";
        }
        return false;
    }
    else
    {
        removeTooltip(ID);
        if(hiddenID != null)
        {
            document.getElementById(hiddenID).value = parseInt(text);
        }
        return true;
    }
}

function addTooltip(id, message)
{
    var element = document.getElementById(id);

    removeClass(element, "validate");
    removeClass(element, "valid");
    addClass(element, "tooltipped");
    addClass(element, "invalid");
    element.dataset.tooltip = message;
    element.dataset.position = "bottom";
    element.dataset.delay = "50";
    M.Tooltip.init(element);
}

function removeTooltip(id)
{
    var element = document.getElementById(id);

    removeClass(element, "validate");
    removeClass(element, "invalid");
    removeClass(element, "tooltipped");
    addClass(element, "valid");
    var tooltip = M.Tooltip.getInstance(element);
    if(tooltip !== undefined)
    {
        tooltip.destroy();
    }
}