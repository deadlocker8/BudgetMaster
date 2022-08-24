$(document).ready(function()
{
    $('.collapsible').collapsible();

    $('.modal').modal('open');

    $('#button-confirm-database-delete').click(function()
    {
        document.getElementById("form-confirm-database-delete").submit();
    });

    $('#button-confirm-database-import').click(function()
    {
        document.getElementById("form-database-import").submit();
    });

    $('#verificationCode').click(function()
    {
        let verificationCodeElement = document.getElementsByName('verificationCode')[0];
        verificationCodeElement.type = 'text';
        verificationCodeElement.select();
        document.execCommand('copy');
        verificationCodeElement.type = 'hidden';

        M.toast({html: copiedToClipboard, classes: 'green'});
    });
});

function toggleSettingsContainerHeader(id, hide)
{
    document.querySelector('#' + id + ' .collapsible-header-button').classList.toggle('hidden', hide);
}