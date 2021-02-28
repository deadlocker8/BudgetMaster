<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('search')}"/>
        <@header.style "transactions"/>
        <@header.style "categories"/>
        <@header.style "search"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "home" settings/>

        <#import "../transactions/transactionsMacros.ftl" as transactionsMacros>
        <#import "searchMacros.ftl" as searchMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.search.results", page.getTotalElements())}</div>
                    </div>
                </div>

                <@header.content>
                    <form id="searchForm" action="<@s.url '/search'/>" method="get">
                        <@searchMacros.searchTextAndButton search/>
                        <@searchMacros.checkboxes search/>
                        <input type="hidden" name="page" id="inputPageNumber" value="${page.getNumber()}"/>
                    </form>

                    <@searchMacros.pagination page/>

                    <div class="row search-container">
                        <div class="col s12">
                            <#list page.getContent() as transaction>
                                <div class="card-panel search-result">
                                    <div class="hide-on-large-only">
                                        <div class="row valign-wrapper">
                                            <div class="col s3 center-align bold transaction-text">
                                                ${dateService.getDateStringNormal(transaction.date)}
                                            </div>
                                            <@transactionsMacros.transactionType transaction/>
                                            <@transactionsMacros.transactionAccount transaction/>
                                            <@transactionsMacros.transactionLinks transaction/>
                                        </div>
                                        <div class="row valign-wrapper no-margin-bottom">
                                            <@transactionsMacros.transactionCategory transaction "center-align"/>
                                            <@transactionsMacros.transactionNameAndDescription transaction "s5"/>
                                            <@transactionsMacros.transactionAmount transaction transaction.getAccount() "s4"/>
                                        </div>
                                    </div>
                                    <div class="hide-on-med-and-down">
                                        <div class="row valign-wrapper no-margin-bottom">
                                            <div class="col l2 xl1 bold transaction-text transaction-line-height transaction-date">
                                                ${dateService.getDateStringNormal(transaction.date)}
                                            </div>
                                            <@transactionsMacros.transactionCategory transaction "left-align"/>
                                            <@transactionsMacros.transactionType transaction/>
                                            <@transactionsMacros.transactionAccount transaction/>
                                            <@transactionsMacros.transactionNameAndDescription transaction "l3 xl4"/>
                                            <@transactionsMacros.transactionAmount transaction transaction.getAccount() "l2 xl2"/>
                                            <@transactionsMacros.transactionLinks transaction/>
                                        </div>
                                    </div>
                                </div>
                            </#list>

                            <#-- placeholder -->
                            <#if page.getContent()?size == 0>
                                <div class="row">
                                    <div class="col s12">
                                        <br><br>
                                        <div class="headline-advice center-align">${locale.getString("search.placeholder")}</div>
                                    </div>
                                </div>
                            </#if>
                        </div>
                    </div>
                </@header.content>
            </div>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/search.js'/>"></script>
    </@header.body>
</html>