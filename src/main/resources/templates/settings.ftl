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
                                        <input type="checkbox" name="isPayment" <#if settings.isRestActivated()>checked</#if>>
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
                                        <input type="checkbox" name="isPayment" <#if settings.isUseDarkTheme()>checked</#if>>
                                        <span class="lever"></span>
                                    ${locale.getString("settings.darkTheme.activated")}
                                    </label>
                                </div>
                            </div>
                        </div>

                        <#-- language -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <select id="settings-language" name="language" <@validation.validation "language"/>>
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
                                                    <input type="checkbox" name="isPayment" <#if settings.isUseDarkTheme()>checked</#if>>
                                                    <span class="lever"></span>
                                                ${locale.getString("settings.updates.automatic.activated")}
                                                </label>
                                            </div>
                                        </td>
                                        <td>${locale.getString("settings.updates.current.version")}</td>
                                        <td>v2.0.0</td>
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
            </div>
        </main>

        <!-- Scripts-->
        <#import "scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="/js/spectrum.js"></script>
        <script src="/js/categories.js"></script>
    </body>
</html>