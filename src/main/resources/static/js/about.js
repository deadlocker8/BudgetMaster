$(document).ready(function()
{
    $('.whatsNewLink').click(function()
    {
        fetchWhatsNewModal(this);
    });
});

function fetchWhatsNewModal(item)
{
    $.ajax({
        type: 'GET',
        url: $(item).attr('data-url'),
        data: {},
        success: function(data)
        {
            let modalID = '#modalWhatsNew';

            $('#whatsNewModelContainer').html(data);
            $(modalID).modal();
            $(modalID).modal('open');
        }
    });
}
