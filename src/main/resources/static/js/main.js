$( document ).ready(function() {
    $("#mobile-menu").sideNav({
        menuWidth: 350 // Default is 300
    });

    $('.modal').modal();

    $('select').material_select();

    $("#selectAccount").on('change', function()
    {
        var accountID = $(this).val();
        window.location = "/account/" + accountID + "/select";
    });
});

function addClass(element, className)
{
    if(element != null)
    {
        if(!element.classList.contains(className))
        {
            element.classList.add(className);
        }
    }
}

function removeClass(element, className)
{
    if(element != null)
    {
        if(element.classList.contains(className))
        {
            element.classList.remove(className);
        }
    }
}

function rgb2hex(rgb)
{
    rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    function hex(x) {
        return ("0" + parseInt(x).toString(16)).slice(-2);
    }
    return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
}