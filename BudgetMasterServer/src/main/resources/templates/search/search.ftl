<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('search')}"/>
        <@header.style "datepicker"/>
        <@header.style "transactions"/>
        <@header.style "search"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "home" settings/>

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
                        <@searchMacros.dateRange search/>
                        <input type="hidden" name="page" id="inputPageNumber" value="${page.getNumber()}"/>
                    </form>

                    <@searchMacros.pagination page "top"/>

                    <div class="row search-container">
                        <div class="col s12">
                            <@searchMacros.renderTransactions page.getContent()/>

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

                    <@searchMacros.pagination page "bottom"/>
                </@header.content>
            </div>
        </main>

        <!-- Pass localization to JS -->
        <#import "../helpers/globalDatePicker.ftl" as datePicker>
        <@datePicker.datePickerLocalization/>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/search.js'/>"></script>
    </@header.body>
</html>