<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "settings" settings/>

        <#import "settingsMacros.ftl" as settingsMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.settings")}</div>
                    </div>
                </div>
                <div class="container">
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="Settings" action="<@s.url '/settings/save'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="${settings.getID()?c}">
                        <input type="hidden" name="lastBackupReminderDate" value="${dateService.getLongDateString(settings.getLastBackupReminderDate())}">

                        <#-- password -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="settings-password" type="password" name="password" <@validation.validation "password"/> value="•••••">
                                <label for="settings-password">${locale.getString("settings.password")}</label>
                            </div>
                        </div>

                        <#-- password confirmation-->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="settings-password-confirmation" type="password" name="passwordConfirmation" <@validation.validation "passwordConfirmation"/> value="•••••">
                                <label for="settings-password-confirmation">${locale.getString("settings.password.confirmation")}</label>
                            </div>
                        </div>

                        <#-- currency -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="settings-currency" type="text" name="currency" <@validation.validation "currency"/> value="<#if settings.getCurrency()??>${settings.getCurrency()}</#if>">
                                <label for="settings-currency">${locale.getString("settings.currency")}</label>
                            </div>
                        </div>

                        <#-- rest, dark theme and backup reminder switch -->
                        <@settingsMacros.switches settings/>

                        <#-- language -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
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

                        <#-- updates -->
                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("settings.updates")}
                            </div>
                        </div>

                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2">
                                <table class="no-border-table">
                                    <tr>
                                        <td rowspan="2">
                                            <div class="switch">
                                                <label>
                                                ${locale.getString("settings.updates.automatic.deactivated")}
                                                    <input type="checkbox" name="autoUpdateCheckEnabled" <#if settings.isAutoUpdateCheckEnabled()>checked</#if>>
                                                    <span class="lever"></span>
                                                ${locale.getString("settings.updates.automatic.activated")}
                                                </label>
                                            </div>
                                        </td>
                                        <td>${locale.getString("settings.updates.current.version")}</td>
                                        <td>v${build.getVersionName()}</td>
                                    </tr>
                                    <tr>
                                        <td>${locale.getString("settings.updates.latest.version")}</td>
                                        <td>${helpers.getAvailableVersionString()}</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <a href="<@s.url '/updateSearch'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">refresh</i>${locale.getString("settings.updates.search")}</a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>

                        <br>

                        <#-- buttons -->
                        <div class="row">
                            <div class="col s12 center-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">save</i>${locale.getString("save")}
                                </button>
                            </div>
                        </div>
                    </form>
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
        <script src="<@s.url '/js/spectrum.js'/>"></script>
        <script src="<@s.url '/js/settings.js'/>"></script>
    </body>
</html>
