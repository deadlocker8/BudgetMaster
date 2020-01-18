$(document).ready(function()
{
    if($('#modalConfirmDelete').length)
    {
        $('#modalConfirmDelete').modal('open');
    }

    if($('#modalAccountNotDeletable').length)
    {
        $('#modalAccountNotDeletable').modal('open');
    }

    if($('#account-name').length)
    {
        document.getElementById('account-name').focus();
    }
});