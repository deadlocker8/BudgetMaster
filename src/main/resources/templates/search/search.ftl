<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "transactions"/>
        <@header.style "categories"/>
        <@header.style "search"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "home" settings/>

        <#import "../transactions/transactionsMacros.ftl" as transactionsMacros>
        <#import "searchMacros.ftl" as searchMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.search.results")}</div>
                    </div>
                </div>
                <form name="NewSearch" action="<@s.url '/search'/>" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <@searchMacros.searchTextAndButton search/>
                    <@searchMacros.checkboxes search/>
                </form>

                <div class="row search-container">
                    <div class="col s12">
                        <#list transactions as transaction>
                            <div class="card-panel search-result">
                                <div class="hide-on-large-only">
                                    <div class="row valign-wrapper">
                                        <div class="col s3 center-align bold transaction-text">
                                            ${dateService.getDateStringWithoutYear(transaction.date)}
                                        </div>
                                        <@transactionsMacros.transactionType transaction/>
                                        <@transactionsMacros.transactionLinks transaction/>
                                    </div>
                                    <div class="row valign-wrapper no-margin-bottom">
                                        <@transactionsMacros.transactionCategory transaction "center-align"/>
                                        <@transactionsMacros.transactionNameAndDescription transaction/>
                                        <@transactionsMacros.transactionAmount transaction transaction.getAccount()/>
                                    </div>
                                </div>
                                <div class="hide-on-med-and-down">
                                    <div class="row valign-wrapper no-margin-bottom">
                                        <div class="col l1 xl1 bold transaction-text transaction-line-height">
                                            ${dateService.getDateStringWithoutYear(transaction.date)}
                                        </div>
                                        <@transactionsMacros.transactionCategory transaction "left-align"/>
                                        <@transactionsMacros.transactionType transaction/>
                                        <@transactionsMacros.transactionNameAndDescription transaction/>
                                        <@transactionsMacros.transactionAmount transaction transaction.getAccount()/>
                                        <@transactionsMacros.transactionLinks transaction/>
                                    </div>
                                </div>
                            </div>
                        </#list>

                        <#-- placeholder -->
                        <#if transactions?size == 0>
                            <div class="row">
                                <div class="col s12">
                                    <br><br>
                                    <div class="headline-advice center-align">${locale.getString("search.placeholder")}</div>
                                </div>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>