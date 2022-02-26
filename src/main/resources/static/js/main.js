$(document).ready(function()
{
    $('#button-logout').click(function()
    {
        document.getElementById('logout-form').submit();
    });

    $('#globalAccountSelect').click(function()
    {
        fetchAndShowModal(this, 'globalAccountSelectModalOnDemand', '#modalGlobalAccountSelect');
    });

    $('.sidenav').sidenav();

    $('.modal').modal();

    if($("#modalBackupReminder").length)
    {
        $('#modalBackupReminder').modal('open');
    }

    if($("#whatsNewModelContainer").length)
    {
        fetchAndShowModal(document.getElementById('whatsNewModelContainer'), 'whatsNewModelContainer', '#modalWhatsNew');
    }

    $('.tooltipped').tooltip();

    $('select').formSelect();

    if($("#login-password").length)
    {
        document.getElementById("login-password").focus();
    }

    $("#buttonSearch").click(function()
    {
        document.getElementById("navbarSearchForm").submit();
    });

    $("#buttonClearSearch").click(function()
    {
        document.getElementById("search").value = "";
    });

    $('.notification-clear').click(function()
    {
        document.getElementById(this.dataset.id).style.display = "none";
    });

    $('.hint-clear').click(function()
    {
        document.getElementById(this.dataset.id).style.display = "none";

        $.ajax({
            type: 'GET',
            url: $(this).attr('data-url'),
            data: {}
        });
    });
});

function fetchAndShowModal(item, containerID, modalID)
{
    let modal = $(modalID).modal();
    if(modal.isOpen)
    {
        return;
    }

    $.ajax({
        type: 'GET',
        url: $(item).attr('data-url'),
        data: {},
        success: function(data)
        {
            $('#' + containerID).html(data);
            $(modalID).modal();
            $(modalID).modal('open');
        }
    });
}

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
    let rgba = rgb.match(/^rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,\s*(\d+\.{0,1}\d*))?\)$/).slice(1);
    let convertedParts = rgba.map((n, i) => (i === 3 ? Math.round(parseFloat(n) * 255) : parseFloat(n))
            .toString(16)
            .padStart(2, '0')
            .replace('NaN', ''));

    return '#' + convertedParts.join('');
}

function validateLoginForm()
{
    let passwordInput = document.getElementById('login-password');
    passwordInput.value = passwordInput.value.trim();
}
