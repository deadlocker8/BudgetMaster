<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header "BudgetMaster"/>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar "settings"/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("info.title.database.import.dialog")}</div>
                        <div>${locale.getString("info.subtitle.database.import")}</div>
                    </div>
                </div>
                <div class="container">
                    <#import "validation.ftl" as validation>
                    <form name="Import" action="/settings/database/import" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <table class="bordered">
                            <#list helpers.getAccountMatches(database.getAccounts()) as accountMatch>
                                <tr>
                                    <td class="import-text">${locale.getString("info.database.import.source")}</td>
                                    <td class="account-source-id hidden"><#if accountMatch.getAccountSource().getID()??>${accountMatch.getAccountSource().getID()}<#else>-1</#if> </td>
                                    <td class="account-source">${accountMatch.getAccountSource().getName()}</td>
                                    <td class="import-text">${locale.getString("info.database.import.destination")}</td>
                                    <td>
                                        <select class="account-destination">
                                            <#list availableAccounts as account>
                                                <#if (account.getType().name() == "CUSTOM")>
                                                    <option value="${account.getID()}">${account.getName()}</option>
                                                </#if>
                                            </#list>
                                        </select>
                                    </td>
                                    <td class="import-text">${locale.getString("info.database.import.or")}</td>
                                    <td>
                                        <a href="/accounts/newAccount" class="btn waves-effect waves-light budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.account.new")}</a>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                        <#if availableAccounts?size == 0>
                            <div class="headline center-align">${locale.getString("placeholder")}</div>
                        </#if>

                        <div id="hidden-account-matches"></div>

                        <br>

                        <#-- buttons -->
                        <div class="row">
                            <div class="col m6 l4 offset-l2 right-align">
                                <a href="/settings" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                            </div>

                            <div class="col m6 l4 left-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">unarchive</i>${locale.getString("settings.database.import")}
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
        <script src="/js/import.js"></script>
    </body>
</html>