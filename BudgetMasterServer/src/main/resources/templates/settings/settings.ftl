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

                                    <@settingsMacros.settingsCollapsibleItem "" "format_paint" locale.getString("settings.appearance")>
                                        <#-- language -->
                                        <div class="row">
                                            <div class="input-field col s12 m12 l8 offset-l2">
                                                <i class="material-icons prefix">translate</i>
                                                <select id="settings-language" name="languageType" <@validation.validation "language"/>>
                                                    <#list helpers.getAvailableLanguages() as language>
                                                        <#if settings.getLanguage() == language>
                                                            <option selected value="${language.getName()}">${language.getName()}</option>
                                                        <#else>
                                                            <option value="${language.getName()}">${language.getName()}</option>
                                                        </#if>
                                                    </#list>
                                                </select>
                                                <label for="settings-language">${locale.getString("settings.language")}</label>
                                            </div>
                                        </div>

                                        <#-- currency -->
                                        <div class="row">
                                            <div class="input-field col s12 m12 l8 offset-l2">
                                                <i class="material-icons prefix">euro</i>
                                                <input id="settings-currency" type="text" name="currency" <@validation.validation "currency"/> value="<#if settings.getCurrency()??>${settings.getCurrency()}</#if>">
                                                <label for="settings-currency">${locale.getString("settings.currency")}</label>
                                            </div>
                                        </div>

                                        <#-- rest, dark theme and category style -->
                                        <@settingsMacros.switches settings/>

                                        <#-- search items per page -->
                                        <div class="row">
                                            <div class="input-field col s12 m12 l8 offset-l2">
                                                <i class="material-icons prefix">search</i>
                                                <select id="settings-search-items-per-page" name="searchItemsPerPage" <@validation.validation "searchItemsPerPage"/>>
                                                    <#list searchResultsPerPageOptions as number>
                                                        <#if settings.getSearchItemsPerPage() == number>
                                                            <option selected value="${number}">${number}</option>
                                                        <#else>
                                                            <option value="${number}">${number}</option>
                                                        </#if>
                                                    </#list>
                                                </select>
                                                <label for="settings-search-items-per-page">${locale.getString("settings.search.itemsPerPage")}</label>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col s12 center-align">
                                                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green'/>
                                            </div>
                                        </div>
                                    </@settingsMacros.settingsCollapsibleItem>

                                    <@settingsMacros.settingsCollapsibleItem "" "list" "Transactions">
                                        <div class="row">
                                            <div class="col s12">
                                                <div class="table-container">
                                                    <div class="table-cell">
                                                        <div class="switch-cell-margin">${locale.getString("settings.rest")}</div>
                                                        <div class="switch-cell-margin">Warn about expenditure switch</div>
                                                    </div>
                                                    <div class="table-cell table-cell-spacer"></div>
                                                    <div class="table-cell">
                                                        <@settingsMacros.switch "rest" "restActivated" settings.isRestActivated()/>
                                                        <@settingsMacros.switch "rest" "restActivated" settings.isRestActivated()/>
                                                    </div>
                                                    <div class="table-cell table-cell-spacer"></div>
                                                    <div class="table-cell">
                                                        <div class="switch-cell-margin">
                                                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.rest.description")}"><i class="material-icons">help_outline</i></a>
                                                        </div>
                                                        <div class="switch-cell-margin">
                                                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.rest.description")}"><i class="material-icons">help_outline</i></a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col s12 center-align">
                                                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green'/>
                                            </div>
                                        </div>
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

                                    <@settingsMacros.settingsCollapsibleItem "" "system_update" locale.getString("settings.updates")>
                                        <div class="row">
                                            <div class="col s12 m12 l8 offset-l2 center-align">
                                                <div class="table-container">
                                                    <div class="table-cell">
                                                        <div class="right-align" style="margin-bottom: 1em;">${locale.getString("settings.updates.current.version")}</div>
                                                        <div class="right-align">${locale.getString("settings.updates.latest.version")}</div>
                                                    </div>

                                                    <div class="table-cell table-cell-spacer"></div>

                                                    <div class="table-cell">
                                                        <div class="left-align" style="margin-bottom: 1em; margin-right: 5em">
                                                            <div class="banner background-grey <#if settings.isUseDarkTheme()>text-black<#else>text-white</#if>">
                                                                v${build.getVersionName()}
                                                            </div>
                                                        </div>
                                                        <div class="left-align">
                                                            <#if updateService.getAvailableVersionString() == "-">
                                                                <#if settings.isUseDarkTheme()>
                                                                    <#assign bannerClasses="background-grey text-black">
                                                                <#else>
                                                                    <#assign bannerClasses="background-grey text-white">
                                                                </#if>
                                                            <#else>
                                                                <#if updateService.isUpdateAvailable()>
                                                                    <#assign bannerClasses="background-orange text-black">
                                                                <#else>
                                                                    <#assign bannerClasses="background-green text-white">
                                                                </#if>
                                                            </#if>

                                                            <div class="banner ${bannerClasses}">
                                                                ${updateService.getAvailableVersionString()}
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="table-cell table-cell-valign">
                                                        <@header.buttonLink url='/settings/updateSearch' icon='refresh' localizationKey='settings.updates.search'/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col s12">
                                                <div class="table-container">
                                                    <div class="table-cell">
                                                        <div class="switch-cell-margin">${locale.getString("settings.updates.automatic")}</div>
                                                    </div>
                                                    <div class="table-cell table-cell-spacer"></div>
                                                    <div class="table-cell">
                                                        <@settingsMacros.switch "updates.automatic" "autoUpdateCheckEnabled" settings.isAutoUpdateCheckEnabled()/>
                                                    </div>
                                                    <div class="table-cell table-cell-spacer"></div>
                                                    <div class="table-cell">
                                                        <div class="switch-cell-margin">
                                                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.updates.automatic.description")}"><i class="material-icons">help_outline</i></a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col s12 center-align">
                                                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green'/>
                                            </div>
                                        </div>
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
        </script>
    </@header.body>
</html>
