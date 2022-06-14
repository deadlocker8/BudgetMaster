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
        <#import "containers/settingsBackup.ftl" as settingsBackupMacros>
        <#import "containers/settingsUpdate.ftl" as settingsUpdateMacros>
        <#import "containers/settingsMisc.ftl" as settingsMiscMacros>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>

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
                                        <@settingsSecurityMacros.securitySettingsContainer importScripts=false/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "personalizationSettingsContainer" "format_paint" locale.getString("settings.personalization")>
                                        <@settingsPersonalizationMacros.personalizationSettingsContainer importScripts=false settings=settings showReloadWarning=false/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "transactionsSettingsContainer" "list" locale.getString("settings.transactions")>
                                        <@settingsTransactionsMacros.transactionsSettingsContainer importScripts=false settings=settings/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "backupSettingsContainer" "cloud_download" locale.getString("settings.backup")>
                                        <@settingsBackupMacros.backupSettingsContainer importScripts=false settings=settings/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "updateSettingsContainer" "system_update" locale.getString("settings.updates")>
                                        <@settingsUpdateMacros.updateSettingsContainer importScripts=false settings=settings/>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "miscSettingsContainer" "miscellaneous_services" locale.getString("settings.misc")>
                                        <@settingsMiscMacros.miscSettingsContainer importScripts=false/>
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
        <script src="<@s.url '/js/helpers.js'/>"></script>
        <script src="<@s.url '/js/settings.js'/>"></script>
        <script src="<@s.url '/js/settingsContainers.js'/>"></script>

        <script>
            initSettingsContainer('SecuritySettingsContainer', 'securitySettingsContainer');
            initSettingsContainer('PersonalizationSettingsContainer', 'personalizationSettingsContainer');
            initSettingsContainer('TransactionsSettingsContainer', 'transactionsSettingsContainer');
            initSettingsContainer('BackupSettingsContainer', 'backupSettingsContainer');
            initSettingsContainer('MiscSettingsContainer', 'miscSettingsContainer');
            initSettingsContainer('UpdateSettingsContainer', 'updateSettingsContainer');
        </script>
    </@header.body>
</html>
