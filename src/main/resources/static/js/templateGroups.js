$(document).ready(function()
{
    if($("#template-group-name").length)
    {
        document.getElementById('template-group-name').focus();
    }

    $('.button-request-delete-template-group').click(function()
    {
        fetchAndShowModalContent(this.dataset.url, '#deleteModalContainerOnDemand', '#modalConfirmDelete', function(){});
    });
});
