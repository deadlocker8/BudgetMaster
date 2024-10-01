<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>
<#import "../settingsMacros.ftl" as settingsMacros>

<#macro accountsSettingsContainer importScripts settings>
    <@settingsContainerMacros.settingsContainer 'AccountsSettingsContainer' 'accountsSettingsContainer' importScripts '/settings/save/accounts'>
        <div class="row">
            <div class="col s12">
                <div class="table-container">
                    <div class="table-cell">
                        <div class="switch-cell-margin">${locale.getString("settings.accountEndDateReminder")}</div>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <@settingsMacros.switch "accountEndDateReminder" "accountEndDateReminderActivated" settings.getAccountEndDateReminderActivated()/>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <div class="switch-cell-margin">
                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.accountEndDateReminder.description")}"><i class="material-icons">help_outline</i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green' onclick='return validateAccountsForm()'/>
            </div>
        </div>

        <script>
            function validateAccountsForm()
            {
                return true;
            }

            // toggle unsaved changes warning

            $('input[name="accountEndDateReminderActivated"]').change(function()
            {
                toggleSettingsContainerHeader('accountsSettingsContainerHeader', false);
            });
        </script>
    </@settingsContainerMacros.settingsContainer>
</#macro>

<@accountsSettingsContainer importScripts=true settings=settings/>
