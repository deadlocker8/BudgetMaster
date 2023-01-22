<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('transactions.recurring.headline')}"/>
        <@header.style "transactions"/>
        <@header.style "search"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "recurring" settings/>

        <#import "../search/searchMacros.ftl" as searchMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">repeat</i> ${locale.getString("transactions.recurring.headline")}</div>
                    </div>
                </div>

                <@header.content>
                    <#if transactions?has_content>
                        <@searchMacros.renderTransactions transactions=transactions openLinksInNewTab=false/>
                    <#else>
                        <#-- show placeholde text if there are no active recurring transactions -->
                        <br>
                        <div class="row">
                            <div class="col s12">
                                <div class="headline-small center-align">${locale.getString("transactions.recurring.placeholder")}</div>
                            </div>
                        </div>
                    </#if>
                </@header.content>
            </div>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>