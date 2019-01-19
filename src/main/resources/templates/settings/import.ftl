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
                        <div class="headline">${locale.getString("info.title.database.import.dialog")}</div>
                        <div>${locale.getString("info.subtitle.database.import")}</div>
                    </div>
                </div>
                <div class="container">
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="Import" action="<@s.url '/settings/database/import'/>" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <table class="bordered">
                            <#list helpers.getAccountMatches(database.getAccounts()) as accountMatch>
                                <tr>
                                    <td class="import-text">${locale.getString("info.database.import.source")}</td>
                                    <td class="account-source-id hidden"><#if accountMatch.getAccountSource().getID()??>${accountMatch.getAccountSource().getID()?c}<#else>-1</#if> </td>
                                    <td class="account-source">${accountMatch.getAccountSource().getName()}</td>
                                    <td class="import-text">${locale.getString("info.database.import.destination")}</td>
                                    <td>
                                        <select class="account-destination">
                                            <#list availableAccounts as account>
                                                <#if (account.getType().name() == "CUSTOM")>
                                                    <option value="${account.getID()?c}">${account.getName()}</option>
                                                </#if>
                                            </#list>
                                        </select>
                                    </td>
                                    <td class="import-text">${locale.getString("info.database.import.or")}</td>
                                    <td>
                                        <a href="<@s.url '/accounts/newAccount'/>" class="btn waves-effect waves-light budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.account.new")}</a>
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
                                <a href="<@s.url '/settings'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
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
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/import.js'/>"></script>
    </body>
</html>