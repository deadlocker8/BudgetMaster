<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>
<#import "../settingsMacros.ftl" as settingsMacros>

<#macro miscSettingsContainer importScripts>
    <@settingsContainerMacros.settingsContainer 'MiscSettingsContainer' 'miscSettingsContainer' importScripts>
        <div class="row no-margin-bottom">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='restore' localizationKey='button.hints.reset' color='background-blue' formaction='/settings/save/misc'/>
            </div>
        </div>
    </@settingsContainerMacros.settingsContainer>
</#macro>

<@miscSettingsContainer importScripts=true/>
