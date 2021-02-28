<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.settings')}"/>
        <@header.style "settings"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "settings" settings/>

        <#import "settingsMacros.ftl" as settingsMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">settings</i> ${locale.getString("menu.settings")}</div>
                    </div>
                </div>

                <@header.content>
                    <div class="container">
                        <#import "../helpers/validation.ftl" as validation>
                        <form name="Settings" action="<@s.url '/settings/save'/>" method="post" onsubmit="return validateForm()">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" id="token"/>
                            <input type="hidden" name="ID" value="${settings.getID()?c}">
                            <input type="hidden" name="lastBackupReminderDate" value="${dateService.getLongDateString(settings.getLastBackupReminderDate())}">
                            <input type="hidden" name="installedVersionCode" value="${settings.getInstalledVersionCode()}">
                            <input type="hidden" name="whatsNewShownForCurrentVersion" value="${settings.getWhatsNewShownForCurrentVersion()?c}">
                            <input type="hidden" name="showFirstUseBanner" value="${settings.getShowFirstUseBanner()?c}">

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

                            <#-- currency -->
                            <div class="row">
                                <div class="input-field col s12 m12 l8 offset-l2">
                                    <i class="material-icons prefix">euro</i>
                                    <input id="settings-currency" type="text" name="currency" <@validation.validation "currency"/> value="<#if settings.getCurrency()??>${settings.getCurrency()}</#if>">
                                    <label for="settings-currency">${locale.getString("settings.currency")}</label>
                                </div>
                            </div>

                            <#-- rest, dark theme and backup reminder switch -->
                            <@settingsMacros.switches settings/>

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

                            <#-- backups -->
                            <div class="container">
                                <div class="section center-align">
                                    <div class="headline">${locale.getString("settings.backup")}</div>
                                </div>
                            </div>

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

                            <br>

                            <#-- buttons -->
                            <div class="row">
                                <div class="col s12 center-align">
                                    <@header.buttonSubmit name='action' icon='save' localizationKey='save'/>
                                </div>
                            </div>
                        </form>
                    </div>

                    <hr>

                    <#-- updates -->
                    <div class="container">
                        <div class="section center-align">
                            <div class="headline">${locale.getString("settings.updates")}</div>
                        </div>
                    </div>

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
                                        <#if updateCheckService.getAvailableVersionString() == "-">
                                            <#if settings.isUseDarkTheme()>
                                                <#assign bannerClasses="background-grey text-black">
                                            <#else>
                                                <#assign bannerClasses="background-grey text-white">
                                            </#if>
                                        <#else>
                                            <#if updateCheckService.isUpdateAvailable()>
                                                <#assign bannerClasses="background-orange text-black">
                                            <#else>
                                                <#assign bannerClasses="background-green text-white">
                                            </#if>
                                        </#if>

                                        <div class="banner ${bannerClasses}">
                                            ${updateCheckService.getAvailableVersionString()}
                                        </div>
                                  </div>
                                </div>

                                <div class="table-cell table-cell-valign">
                                    <@header.buttonLink url='/settings/updateSearch' icon='refresh' localizationKey='settings.updates.search'/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <hr>
                    <#-- database -->
                    <div class="container">
                        <div class="section center-align">
                            <div class="headline">${locale.getString("menu.settings.database")}</div>
                        </div>
                    </div>
                    <@settingsMacros.databaseNormal/>
                    <@settingsMacros.databaseSmall/>
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

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/libs/spectrum.js'/>"></script>
        <script src="<@s.url '/js/helpers.js'/>"></script>
        <script src="<@s.url '/js/settings.js'/>"></script>
    </@header.body>
</html>
