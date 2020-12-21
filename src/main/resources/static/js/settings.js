$(document).ready(function()
{
    $('.modal').modal('open');

    $('#button-confirm-database-delete').click(function()
    {
        document.getElementById("form-confirm-database-delete").submit();
    });

    $('#button-confirm-database-import').click(function()
    {
        document.getElementById("form-database-import").submit();
    });

    $('input[name="autoBackupActivated"]').click(function()
    {
        $('#settings-auto-backup').toggle($(this).prop("checked"));
    });

    $('#settings-backup-auto-strategy').change(function()
    {
        onAutoBackupStrategyChange(this.selectedIndex);
    });

    $('#settings-backup-auto-git-test').click(function()
    {
        $.ajax({
            type: 'POST',
            url: $('#settings-backup-auto-git-test').attr('data-url'),
            data: {
                '_csrf': document.getElementById('token').value,
                'autoBackupGitUrl': document.getElementById('settings-backup-auto-git-url').value,
                'autoBackupGitUserName': document.getElementById('settings-backup-auto-git-user-name').value,
                'autoBackupGitPassword': document.getElementById('settings-backup-auto-git-password').value,
            },
            success: function(data)
            {
                M.toast({html: data});
            },
            error: function(data)
            {
                console.log(data);
            }
        });
    });

    let autoBackupDays = $('#settings-backup-auto-days');
    if(autoBackupDays.length)
    {
        autoBackupDays.on('change keydown paste input', function()
        {
            validateNumber(autoBackupDays.val(), 'settings-backup-auto-days', "hidden-settings-backup-auto-days", numberValidationMessage, REGEX_NUMBER_GREATER_ZERO);
        });
    }

    let autoBackupFilesToKeep = $('#settings-backup-auto-files-to-keep');
    if(autoBackupFilesToKeep.length)
    {
        autoBackupFilesToKeep.on('change keydown paste input', function()
        {
            validateNumber(autoBackupFilesToKeep.val(), "settings-backup-auto-files-to-keep", "hidden-settings-backup-auto-files-to-keep", numberValidationMessageZeroAllowed, REGEX_NUMBER);
        });
    }

    // on initial page load
    let autoBackupCheckbox = document.getElementsByName("autoBackupActivated")[0];
    $('#settings-auto-backup').toggle(autoBackupCheckbox.checked);
    onAutoBackupStrategyChange(document.getElementById('settings-backup-auto-strategy').selectedIndex);
});

function validateForm()
{
    let autoBackupCheckbox = document.getElementsByName("autoBackupActivated")[0];
    if(autoBackupCheckbox.checked)
    {
        let autoBackupDaysValid = validateNumber($('#settings-backup-auto-days').val(), "settings-backup-auto-days", "hidden-settings-backup-auto-days", numberValidationMessage, REGEX_NUMBER_GREATER_ZERO);
        let autoBackupFilesToKeepValid = validateNumber($('#settings-backup-auto-files-to-keep').val(), "settings-backup-auto-files-to-keep", "hidden-settings-backup-auto-files-to-keep", numberValidationMessageZeroAllowed, REGEX_NUMBER);
        return autoBackupDaysValid && autoBackupFilesToKeepValid;
    }

    return true;
}

function onAutoBackupStrategyChange(newSelectedIndex)
{
    $('#settings-auto-backup-local').toggle(newSelectedIndex === 0);  // local backup with file system copies
    // index 1 --> git local doesn't have any settings
    $('#settings-auto-backup-git-remote').toggle(newSelectedIndex === 2);  // git remote
}