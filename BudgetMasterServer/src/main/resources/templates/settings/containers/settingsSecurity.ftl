<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>

<#macro securitySettingsContainer importScripts>
    <@settingsContainerMacros.settingsContainer 'SecuritySettingsContainer' 'securitySettingsContainer' importScripts>
        <#-- password -->
        <div class="row">
            <div class="input-field col s12 m12 l8 offset-l2">
                <i class="material-icons prefix">vpn_key</i>
                <input id="settings-password" type="password" name="password" <@validation.validation "password"/> value="•••••">
                <label for="settings-password">${locale.getString("settings.password")}</label>
            </div>
        </div>

        <#-- password confirmation-->
        <div class="row">
            <div class="input-field col s12 m12 l8 offset-l2">
                <i class="material-icons prefix">vpn_key</i>
                <input id="settings-password-confirmation" type="password" name="passwordConfirmation" <@validation.validation "passwordConfirmation"/> value="•••••">
                <label for="settings-password-confirmation">${locale.getString("settings.password.confirmation")}</label>
            </div>
        </div>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green' formaction='/settings/save/security'/>
            </div>
        </div>

        <script>
            $('#settings-password').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('securitySettingsContainerHeader', false);
            });
            $('#settings-password-confirmation').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('securitySettingsContainerHeader', false);
            });
        </script>
    </@settingsContainerMacros.settingsContainer>
</#macro>

<@securitySettingsContainer importScripts=true/>