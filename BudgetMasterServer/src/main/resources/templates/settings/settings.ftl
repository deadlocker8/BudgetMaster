<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.settings')}"/>
        <@header.style "settings"/>
        <@header.style "collapsible"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "settings" settings/>

        <#import "../helpers/validation.ftl" as validation>
        <#import "settingsMacros.ftl" as settingsMacros>

        <#import "containers/settingsSecurity.ftl" as settingsSecurityMacros>
        <#import "containers/settingsPersonalization.ftl" as settingsPersonalizationMacros>
        <#import "containers/settingsTransactions.ftl" as settingsTransactionsMacros>
        <#import "containers/settingsUpdate.ftl" as settingsUpdatenMacros>


        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">settings</i> ${locale.getString("menu.settings")}</div>
                    </div>
                </div>

                <@header.content>
                    <div class="container">

                        <div class="row">
                            <div class="col s12">
                                <ul class="collapsible">
                                    <@settingsMacros.settingsCollapsibleItem "securitySettingsContainer" "vpn_key" "Security">
                                        <@settingsSecurityMacros.securitySettingsContainer/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "personalizationSettingsContainer" "format_paint" locale.getString("settings.personalization")>
                                        <@settingsPersonalizationMacros.personalizationSettingsContainer settings=settings showReloadWarning=false/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "transactionsSettingsContainer" "list" locale.getString("settings.transactions")>
                                        <@settingsTransactionsMacros.transactionsSettingsContainer settings/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "" "cloud_download" locale.getString("settings.backup")>
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
                                                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green'/>
                                            </div>
                                        </div>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "updateSettingsContainer" "system_update" locale.getString("settings.updates")>
                                        <@settingsUpdatenMacros.updateSettingsContainer settings/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "" "miscellaneous_services" "Misc">
                                        <div class="row no-margin-bottom">
                                            <div class="col s12 center-align">
                                                <@header.buttonLink url='/hints/resetAll' icon='restore' localizationKey='button.hints.reset'/>
                                            </div>
                                        </div>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "" "fas fa-database" locale.getString("menu.settings.database") true>
                                        <@settingsMacros.databaseNormal/>
                                        <@settingsMacros.databaseSmall/>
                                    </@settingsMacros.settingsCollapsibleItem>
                                </ul>
                            </div>
                        </div>



                        <form name="Settings" action="<@s.url '/settings/save'/>" method="post" onsubmit="return validateForm()">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" id="token"/>
                            <input type="hidden" name="ID" value="${settings.getID()?c}">
                            <input type="hidden" name="lastBackupReminderDate" value="${dateService.getLongDateString(settings.getLastBackupReminderDate())}">
                            <input type="hidden" name="installedVersionCode" value="${settings.getInstalledVersionCode()}">
                            <input type="hidden" name="whatsNewShownForCurrentVersion" value="${settings.getWhatsNewShownForCurrentVersion()?c}">
                            <input type="hidden" name="migrationDeclined" value="${settings.getMigrationDeclined()?c}">
                        </form>
                    </div>
                </@header.content>
            </div>
        </main>

        <#if deleteDatabase??>
            <@settingsMacros.deleteDB verificationCode/>
        </#if>

        <#if importDatabase??>
           <@settingsMacros.importDB/>
        </#if>

        <#if errorImportDatabase??>
            <@settingsMacros.errorImport errorImportDatabase/>
        </#if>

        <#if performUpdate??>
            <@settingsMacros.update/>
        </#if>

        <script>
            copiedToClipboard = '${locale.getString("copied")}';
        </script>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/helpers.js'/>"></script>
        <script src="<@s.url '/js/settings.js'/>"></script>
        <script src="<@s.url '/js/settingsContainers.js'/>"></script>

        <script>
            initSettingsContainer('SecuritySettingsContainer', 'securitySettingsContainer');
            initSettingsContainer('PersonalizationSettingsContainer', 'personalizationSettingsContainer');
            initSettingsContainer('TransactionsSettingsContainer', 'transactionsSettingsContainer');
            initSettingsContainer('UpdateSettingsContainer', 'updateSettingsContainer');
        </script>
    </@header.body>
</html>
