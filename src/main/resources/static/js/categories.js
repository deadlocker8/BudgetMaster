$( document ).ready(function() {
    $('.modal').modal('open');

    $('.category-color').click(function()
    {
        var colors = document.getElementsByClassName("category-color");
        for(var i=0; i < colors.length; i++)
        {
            removeClass(colors[i], "category-color-active");
        }

        addClass($(this)[0], "category-color-active");

        document.getElementById("categoryColor").value = rgb2hex($(this)[0].style.backgroundColor);
    });
});