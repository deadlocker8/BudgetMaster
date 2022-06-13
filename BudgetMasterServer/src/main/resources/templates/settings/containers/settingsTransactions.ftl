<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>
<#import "../settingsMacros.ftl" as settingsMacros>

<#macro transactionsSettingsContainer settings>
    <@settingsContainerMacros.settingsContainer 'TransactionsSettingsContainer' 'transactionsSettingsContainer'>
        <div class="row">
            <div class="col s12">
                <div class="table-container">
                    <div class="table-cell">
                        <div class="switch-cell-margin">${locale.getString("settings.rest")}</div>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <@settingsMacros.switch "rest" "restActivated" settings.isRestActivated()/>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <div class="switch-cell-margin">
                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.rest.description")}"><i class="material-icons">help_outline</i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green' formaction='/settings/save/transactions'/>
            </div>
        </div>
    </@settingsContainerMacros.settingsContainer>
</#macro>

<@transactionsSettingsContainer settings/>
