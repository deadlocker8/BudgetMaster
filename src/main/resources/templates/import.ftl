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
                    <form name="Import" action="/settings/database/import" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <table class="bordered">
                            <#list helpers.getAccountMatches(database.getAccounts()) as accountMatch>
                                <tr>
                                    <td class="import-text">${locale.getString("info.database.import.source")}</td>
                                    <td>${accountMatch.getAccountSource().getName()}</td>
                                    <td class="import-text">${locale.getString("info.database.import.destination")}</td>
                                    <td>
                                        <select>
                                            <#list availableAccounts as account>
                                                <option value="${account.getName()}">${account.getName()}</option>
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

                        <br>

                        <#-- buttons -->
                        <div class="row">
                            <div class="col s12 m12 l4 offset-l4 center-align">
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
    </body>
</html>