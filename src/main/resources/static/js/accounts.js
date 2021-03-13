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

    $('#button-remove-account-icon').click(function()
    {
        document.querySelector(".account-icon-preview").classList.toggle('hidden', true);
        document.getElementById("account-icon-placeholder").classList.toggle('hidden', false);
        document.getElementById("hidden-input-account-icon").value = '';
    });
});