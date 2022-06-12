<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#macro securitySettingsContainer>
    <form name="SecuritySettingsContainer" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" id="token"/>

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
    </form>

    <div class="hidden securityContainerToastContent">
        <#if toastContent??>${toastContent}</#if>
    </div>

    <script src="<@s.url '/webjars/jquery/3.6.0/jquery.min.js'/>"></script>
    <script src="<@s.url '/webjars/materializecss/1.0.0/js/materialize.min.js'/>"></script>
    <script src="<@s.url '/js/settingsContainers/settingsSecurity.js'/>"></script>
</#macro>

<@securitySettingsContainer/>