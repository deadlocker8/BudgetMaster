<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#assign locale = static["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar "settings"/>

        <main>
            <div class="card main-card">
                <div class="container">
                    <div class="section center-align">
                        <div class="grey-text text-darken-4 headline">${locale.getString("menu.settings")}</div>
                    </div>
                </div>
                <div class="container">
                    <#import "validation.ftl" as validation>
                    <form name="Setttings" action="/settings/save" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="${settings.getID()}">

                        <#-- password -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="settings-password" type="text" name="password" <@validation.validation "password"/> value="•••••">
                                <label for="settings-password">${locale.getString("settings.password")}</label>
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
                                <table>
                                    <tr>
                                        <td rowspan="2">
                                            <div class="switch">
                                                <label>
                                                ${locale.getString("settings.updates.automatic.deactivated")}
                                                    <input type="checkbox" name="autoUpdateCheckEnabled" <#if settings.isUseDarkTheme()>checked</#if>>
                                                    <span class="lever"></span>
                                                ${locale.getString("settings.updates.automatic.activated")}
                                                </label>
                                            </div>
                                        </td>
                                        <td>${locale.getString("settings.updates.current.version")}</td>
                                        <td>v${locale.getString("version.name")} (${locale.getString("version.code")})</td>
                                    </tr>
                                    <tr>
                                        <td>${locale.getString("settings.updates.latest.version")}</td>
                                        <td>v2.0.0</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <a href="/updateSearch" class="waves-effect waves-light btn budgetmaster-blue-light"><i class="material-icons left">refresh</i>${locale.getString("settings.updates.search")}</a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>

                        <br>

                        <#-- buttons -->
                        <div class="row">
                            <div class="col s12 m12 l4 offset-l4 center-align">
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
                        <div class="grey-text text-darken-4 headline">${locale.getString("menu.settings.database")}</div>
                    </div>
                </div>
                <div class="container">
                    <div class="row hide-on-small-only">
                        <div class="col m4 l4 center-align">
                            <a href="/settings/database/requestImport" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_upload</i>${locale.getString("settings.database.import")}</a>
                        </div>

                        <div class="col m4 l4 center-align">
                            <a href="/settings/database/requestExport" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_download</i>${locale.getString("settings.database.export")}</a>
                        </div>

                        <div class="col m4 l4 center-align">
                            <a href="/settings/database/requestDelete" class="waves-effect waves-light btn budgetmaster-red"><i class="material-icons left">delete_forever</i>${locale.getString("settings.database.delete")}</a>
                        </div>
                    </div>

                    <div class="hide-on-med-and-up">
                        <div class="row center-align">
                            <div class="col s12">
                                <a href="/settings/database/requestImport" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_upload</i>${locale.getString("settings.database.import")}</a>
                            </div>
                        </div>

                        <div class="row center-align">
                            <div class="col s12">
                                <a href="/settings/database/requestExport" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_download</i>${locale.getString("settings.database.export")}</a>
                            </div>
                        </div>

                        <div class="row center-align">
                            <div class="col s12">
                                <a href="/settings/database/requestDelete" class="waves-effect waves-light btn budgetmaster-red"><i class="material-icons left">delete_forever</i>${locale.getString("settings.database.delete")}</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <#if deleteDatabase??>
        <!-- confirm delete modal -->
        <div id="modalConfirmDelete" class="modal">
            <div class="modal-content">
                <h4>${locale.getString("info.title.database.delete")}</h4>
                <p>${locale.getString("info.header.text.database.delete")}</p>
                <p>${locale.getString("info.text.database.delete", verificationCode)}</p>

                <form id="form-confirm-database-delete" action="/settings/database/delete" method="post">
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
            <div class="modal-footer">
                <a href="/settings" class="modal-action modal-close waves-effect waves-red btn-flat ">${locale.getString("cancel")}</a>
                <a class="modal-action modal-close waves-effect waves-green btn-flat" id="button-confirm-database-delete">${locale.getString("delete")}</a>
            </div>
        </div>
    </#if>

        <!-- Scripts-->
        <#import "scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="/js/spectrum.js"></script>
        <script src="/js/categories.js"></script>
        <script src="/js/settings.js"></script>
    </body>
</html>