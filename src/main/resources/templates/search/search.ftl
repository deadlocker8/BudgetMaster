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

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.search.results")}</div>
                    </div>
                </div>
                <form name="NewSearch" action="<@s.url '/search'/>" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <div class="row no-margin-bottom valign-wrapper">
                        <div class="col s10 m7 offset-m1 l6 offset-l2">
                            <div class="input-field">
                                <input id="searchText" type="text" name="searchText" value="${search.getSearchText()}">
                                <label for="searchText">${locale.getString("search")}</label>
                            </div>
                        </div>

                        <div class="col s2 m3 l4">
                            <div class="hide-on-small-only">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">search</i>${locale.getString("search.submit")}
                                </button>
                            </div>
                            <div class="hide-on-med-and-up">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons">search</i>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col s8 offset-s2 m4 offset-m2 l3 offset-l3">
                            <div class="search-checkbox-container">
                                <label>
                                    <input type="checkbox" name="searchName" <#if search.isSearchName()>checked="checked"</#if>>
                                    <span class="text-color">${locale.getString('search.in.name')}</span>
                                </label>
                            </div>
                        </div>
                        <div class="col s8 offset-s2 m6 l6">
                            <div class="search-checkbox-container">
                                <label>
                                    <input type="checkbox" name="searchDescription" <#if search.isSearchDescription()>checked="checked"</#if>>
                                    <span class="text-color">${locale.getString('search.in.description')}</span>
                                </label>
                            </div>
                        </div>
                        <div class="col s8 offset-s2 m4 offset-m2 l3 offset-l3">
                            <div class="search-checkbox-container">
                                <label>
                                    <input type="checkbox" name="searchCategory" <#if search.isSearchCategory()>checked="checked"</#if>>
                                    <span class="text-color">${locale.getString('search.in.category')}</span>
                                </label>
                            </div>
                        </div>
                        <div class="col s8 offset-s2 m6 l6">
                            <div class="search-checkbox-container">
                                <label>
                                    <input type="checkbox" name="searchTags" <#if search.isSearchTags()>checked="checked"</#if>>
                                    <span class="text-color">${locale.getString('search.in.tags')}</span>
                                </label>
                            </div>
                        </div>
                    </div>
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