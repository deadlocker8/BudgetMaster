$(document).ready(function()
{
    if($('#modalAccountNotDeletable').length)
    {
        $('#modalAccountNotDeletable').modal('open');
    }

    if($('#account-name').length)
    {
        document.getElementById('account-name').focus();
    }

    $('.button-request-delete-account').click(function()
    {
        fetchAndShowModalContent(this.dataset.url, '#deleteModalContainerOnDemand', '#modalConfirmDelete', function(){});
    });
});
