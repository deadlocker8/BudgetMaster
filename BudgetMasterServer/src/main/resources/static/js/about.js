$(document).ready(function()
{
    $('.whatsNewLink').click(function()
    {
        fetchAndShowModal(this, 'whatsNewModelContainerOnDemand', '#modalWhatsNew');
    });
});
