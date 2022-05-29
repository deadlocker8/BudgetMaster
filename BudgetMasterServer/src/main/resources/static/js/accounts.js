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

    $('#account-name').on('change keydown paste input', function()
    {
        let accountName = $(this).val();
        document.getElementById('item-icon-fallback-name').innerText = accountName.charAt(0).toUpperCase();
    });

    $('.button-request-delete-account').click(function()
    {
        fetchAndShowModalContent(this.dataset.url, '#deleteModalContainerOnDemand', '#modalConfirmDelete', function(){});
    });
});
