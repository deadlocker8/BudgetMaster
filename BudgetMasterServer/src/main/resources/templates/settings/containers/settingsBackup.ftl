<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>
<#import "../settingsMacros.ftl" as settingsMacros>

<#macro backupSettingsContainer importScripts settings>
    <@settingsContainerMacros.settingsContainer 'BackupSettingsContainer' 'backupSettingsContainer' importScripts '/settings/save/backup'>
        <div class="row">
            <div class="col s12">
                <div class="table-container">
                    <div class="table-cell">
                        <div class="switch-cell-margin">${locale.getString("settings.backupReminder")}</div>
                        <div class="switch-cell-margin">${locale.getString("settings.backup.auto")}</div>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <@settingsMacros.switch "backupReminder" "backupReminderActivated" settings.getBackupReminderActivated()/>
                        <@settingsMacros.switch "backup.auto" "autoBackupActivated" settings.isAutoBackupActive()/>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <div class="switch-cell-margin">
                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.backupReminder.description")}"><i class="material-icons">help_outline</i></a>
                        </div>
                        <div class="switch-cell-margin">
                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.backup.auto.description")}"><i class="material-icons">help_outline</i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <#-- auto backup -->
        <@settingsMacros.autoBackup/>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green' onclick='return validateBackupForm()'/>
            </div>
        </div>

        <script>
            function onAutoBackupStrategyChange(newSelectedIndex)
            {
                $('#settings-auto-backup-local').toggle(newSelectedIndex === 0);  // local backup with file system copies
                // index 1 --> git local doesn't have any settings
                $('#settings-auto-backup-git-remote').toggle(newSelectedIndex === 2);  // git remote
            }

            function validateBackupForm()
            {
                let autoBackupCheckbox = document.getElementsByName("autoBackupActivated")[0];
                if(autoBackupCheckbox.checked)
                {
                    let autoBackupDaysValid = validateNumber($('#settings-backup-auto-days').val(), "settings-backup-auto-days", "hidden-settings-backup-auto-days", numberValidationMessage, REGEX_NUMBER_GREATER_ZERO);
                    let autoBackupFilesToKeepValid = validateNumber($('#settings-backup-auto-files-to-keep').val(), "settings-backup-auto-files-to-keep", "hidden-settings-backup-auto-files-to-keep", numberValidationMessageZeroAllowed, REGEX_NUMBER);
                    return autoBackupDaysValid && autoBackupFilesToKeepValid;
                }
                else
                {
                    document.getElementById('settings-backup-auto-strategy').name = '';
                }

                return true;
            }

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
                        'autoBackupGitBranchName': document.getElementById('settings-backup-auto-git-branch-name').value,
                        'autoBackupGitUserName': document.getElementById('settings-backup-auto-git-user-name').value,
                        'autoBackupGitToken': document.getElementById('settings-backup-auto-git-token').value,
                    },
                    success: function(data)
                    {
                        let parsedData = JSON.parse(data);
                        let isValidConnection = parsedData['isValidConnection']
                        M.toast({
                            html: parsedData['localizedMessage'],
                            classes: isValidConnection ? 'background-green': 'background-red'
                        });
                    },
                    error: function(data)
                    {
                        M.toast({
                            html: 'Error: ' + data,
                            classes: 'background-red'
                        });
                    }
                });
            });

            var autoBackupDays = $('#settings-backup-auto-days');
            if(autoBackupDays.length)
            {
                autoBackupDays.on('change keydown paste input', function()
                {
                    validateNumber(autoBackupDays.val(), 'settings-backup-auto-days', "hidden-settings-backup-auto-days", numberValidationMessage, REGEX_NUMBER_GREATER_ZERO);
                });
            }

            var autoBackupFilesToKeep = $('#settings-backup-auto-files-to-keep');
            if(autoBackupFilesToKeep.length)
            {
                autoBackupFilesToKeep.on('change keydown paste input', function()
                {
                    validateNumber(autoBackupFilesToKeep.val(), "settings-backup-auto-files-to-keep", "hidden-settings-backup-auto-files-to-keep", numberValidationMessageZeroAllowed, REGEX_NUMBER);
                });
            }

            $('#settings-backup-run-now').click(function()
            {
                document.getElementById('runBackupInput').value = 1;
            });

            var autoBackupCheckbox = document.getElementsByName("autoBackupActivated")[0];
            $('#settings-auto-backup').toggle(autoBackupCheckbox.checked);
            onAutoBackupStrategyChange(document.getElementById('settings-backup-auto-strategy').selectedIndex);


            // toggle unsaved changes warning

            $('input[name="backupReminderActivated"]').change(function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('input[name="autoBackupActivated"]').change(function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('#settings-backup-auto-days').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('#settings-backup-auto-time').change(function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('#settings-backup-auto-strategy').change(function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('#settings-backup-auto-files-to-keep').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('#settings-backup-auto-git-url').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('#settings-backup-auto-git-branch-name').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('#settings-backup-auto-git-user-name').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });

            $('#settings-backup-auto-git-token').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('backupSettingsContainerHeader', false);
            });
        </script>
    </@settingsContainerMacros.settingsContainer>
</#macro>

<@backupSettingsContainer importScripts=true settings=settings/>
