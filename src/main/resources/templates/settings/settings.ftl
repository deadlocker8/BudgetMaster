<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "settings"/>

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

                        <#-- rest and dark theme switch -->
                        <div class="row">
                            <div class="col s6 l4 offset-l2 center-align">
                                ${locale.getString("settings.rest")}
                                <div class="switch">
                                    <label>
                                    ${locale.getString("settings.rest.deactivated")}
                                        <input type="checkbox" name="restActivated" <#if settings.isRestActivated()>checked</#if>>
                                        <span class="lever"></span>
                                    ${locale.getString("settings.rest.activated")}
                                    </label>
                                </div>
                            </div>
                            <div class="col s6 l4 center-align">
                                ${locale.getString("settings.darkTheme")}
                                <div class="switch">
                                    <label>
                                    ${locale.getString("settings.darkTheme.deactivated")}
                                        <input type="checkbox" name="useDarkTheme" <#if settings.isUseDarkTheme()>checked</#if>>
                                        <span class="lever"></span>
                                    ${locale.getString("settings.darkTheme.activated")}
                                    </label>
                                </div>
                            </div>
                        </div>

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
                <div class="row hide-on-small-only">
                    <div class="col m4 l4 center-align">
                        <a href="<@s.url '/settings/database/requestImport'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_upload</i>${locale.getString("settings.database.import")}</a>
                    </div>

                    <div class="col m4 l4 center-align">
                        <a href="<@s.url '/settings/database/requestExport'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_download</i>${locale.getString("settings.database.export")}</a>
                    </div>

                    <div class="col m4 l4 center-align">
                        <a href="<@s.url '/settings/database/requestDelete'/>" class="waves-effect waves-light btn budgetmaster-red"><i class="material-icons left">delete_forever</i>${locale.getString("settings.database.delete")}</a>
                    </div>
                </div>

                <div class="hide-on-med-and-up">
                    <div class="row center-align">
                        <div class="col s12">
                            <a href="<@s.url '/settings/database/requestImport'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_upload</i>${locale.getString("settings.database.import")}</a>
                        </div>
                    </div>

                    <div class="row center-align">
                        <div class="col s12">
                            <a href="<@s.url '/settings/database/requestExport'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_download</i>${locale.getString("settings.database.export")}</a>
                        </div>
                    </div>

                    <div class="row center-align">
                        <div class="col s12">
                            <a href="<@s.url '/settings/database/requestDelete'/>" class="waves-effect waves-light btn budgetmaster-red"><i class="material-icons left">delete_forever</i>${locale.getString("settings.database.delete")}</a>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <#if deleteDatabase??>
            <div id="modalConfirmDelete" class="modal background-color">
                <div class="modal-content">
                    <h4>${locale.getString("info.title.database.delete")}</h4>
                    <p>${locale.getString("info.header.text.database.delete")}</p>
                    <p>${locale.getString("info.text.database.delete", verificationCode)}</p>

                    <form id="form-confirm-database-delete" action="<@s.url '/settings/database/delete'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="verificationCode" value="${verificationCode}"/>

                        <div class="row">
                            <div class="input-field col s12 m8 l6">
                                <input id="verification" type="text" name="verificationUserInput">
                                <label for="verification">${locale.getString("settings.database.delete.verification")}</label>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer background-color">
                    <a href="<@s.url '/settings'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                    <a class="modal-action modal-close waves-effect waves-light green btn-flat white-text" id="button-confirm-database-delete">${locale.getString("delete")}</a>
                </div>
            </div>
        </#if>

        <#if importDatabase??>
            <div id="modalImportDatabase" class="modal background-color">
                <div class="modal-content">
                    <h4>${locale.getString("info.title.database.import.dialog")}</h4>

                    <form id="form-database-import" method="POST" action="<@s.url '/settings/database/upload'/>" enctype="multipart/form-data" accept-charset="UTF-8">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="file-field input-field">
                            <div class="btn budgetmaster-blue">
                                <i class="material-icons">cloud_upload</i>
                                <input type="file" accept=".json" name="file">
                            </div>
                            <div class="file-path-wrapper">
                                <input class="file-path validate" type="text">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer background-color">
                    <a href="<@s.url '/settings'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                    <a class="modal-action modal-close waves-effect waves-light green btn-flat white-text" id="button-confirm-database-import">${locale.getString("settings.database.import")}</a>
                </div>
            </div>
        </#if>

        <#if errorImportDatabase??>
            <div id="modalErrorImportDatabase" class="modal background-color">
                <div class="modal-content">
                    <h4>${locale.getString("error.title.database.import")}</h4>
                    <p>${locale.getString("error.text.database.import", errorImportDatabase)}</p>
                </div>
                <div class="modal-footer background-color">
                    <a href="<@s.url '/settings'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("ok")}</a>
                </div>
            </div>
        </#if>

        <#if performUpdate??>
            <div id="modelPerformUpdate" class="modal background-color">
                <div class="modal-content">
                    <h4>${locale.getString("info.title.update")}</h4>
                    <p>${updateString}</p>
                </div>
                <div class="modal-footer background-color">
                    <a href="<@s.url '/settings'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                    <a href="<@s.url '/performUpdate'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("settings.update.start")}</a>
                </div>
            </div>
        </#if>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/spectrum.js'/>"></script>
        <script src="<@s.url '/js/settings.js'/>"></script>
    </body>
</html>