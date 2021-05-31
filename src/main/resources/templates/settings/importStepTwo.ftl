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
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="Import" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="section center-align">
                            <@header.buttonSubmit name='action' icon='add' localizationKey='title.account.new' id='buttonImportCreateAccount' classes='button-new-account' formaction='/settings/database/import/step2/createAccount'/>
                        </div>

                        <table class="bordered">
                            <#if accountMatchList??>
                                <#assign accountMatches=accountMatchList.getAccountMatches()>
                            <#else>
                                <#assign accountMatches=helpers.getAccountMatches(database.getAccounts())>
                            </#if>

                            <#list accountMatches as accountMatch>
                                <tr>
                                    <td class="import-text">${locale.getString("info.database.import.source")}</td>
                                    <td class="account-source-id hidden"><#if accountMatch.getAccountSource().getID()??>${accountMatch.getAccountSource().getID()?c}<#else>-1</#if> </td>
                                    <td class="account-source">${accountMatch.getAccountSource().getName()}</td>
                                    <td class="import-text">${locale.getString("info.database.import.destination")}</td>
                                    <td>
                                        <div class="input-field no-margin">
                                            <select class="account-destination">
                                                <#if accountMatch.getAccountDestination()??>
                                                    <@accountSelect selectedAccountID=accountMatch.getAccountDestination().getID()/>
                                                <#else>
                                                    <@accountSelect/>
                                                </#if>
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
                                <@header.buttonSubmit name='action' icon='unarchive' localizationKey='settings.database.import' id='buttonImport' formaction='/settings/database/import/step3'/>
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

<#macro accountSelect selectedAccountID=-1>
    <#list availableAccounts as account>
        <#if (account.getType().name() == "CUSTOM")>
            <option value="${account.getID()?c}" <#if account.getID() == selectedAccountID>selected</#if>>${account.getName()}</option>
        </#if>
    </#list>
</#macro>