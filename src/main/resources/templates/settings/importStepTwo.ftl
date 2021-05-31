<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('settings.database.import')}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "settings" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("info.title.database.import.dialog")}</div>
                    </div>
                </div>

                <div class="container">
                    <div class="section center-align">
                        <div class="headline-small">${locale.getString("info.database.import.match.accounts")}</div>
                    </div>
                </div>

                <div class="container">
                    <div class="section center-align">
                        <@header.buttonLink url='/accounts/newAccount' icon='add' localizationKey='title.account.new' classes='button-new-account'/>
                    </div>
                </div>

                <div class="container">
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="Import" action="<@s.url '/settings/database/import/step3'/>" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <table class="bordered">
                            <#list helpers.getAccountMatches(database.getAccounts()) as accountMatch>
                                <tr>
                                    <td class="import-text">${locale.getString("info.database.import.source")}</td>
                                    <td class="account-source-id hidden"><#if accountMatch.getAccountSource().getID()??>${accountMatch.getAccountSource().getID()?c}<#else>-1</#if> </td>
                                    <td class="account-source">${accountMatch.getAccountSource().getName()}</td>
                                    <td class="import-text">${locale.getString("info.database.import.destination")}</td>
                                    <td>
                                        <div class="input-field no-margin">
                                            <select class="account-destination">
                                                <#list availableAccounts as account>
                                                    <#if (account.getType().name() == "CUSTOM")>
                                                        <option value="${account.getID()?c}">${account.getName()}</option>
                                                    </#if>
                                                </#list>
                                            </select>
                                        </div>
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
                                <@header.buttonLink url='/settings' icon='clear' localizationKey='cancel'/>
                            </div>

                            <div class="col m6 l4 left-align">
                                <@header.buttonSubmit name='action' icon='unarchive' localizationKey='settings.database.import' id='buttonImport'/>
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
    </@header.body>
</html>