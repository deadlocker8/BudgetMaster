<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.transactions')}"/>
        <@header.style "transactions"/>
        <@header.style "globalDatepicker"/>
        <@header.style "collapsible"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "transactions" settings/>

        <#import "transactionsMacros.ftl" as transactionsMacros>
        <#import "../filter/filterMacros.ftl" as filterMacros>

        <main>
            <div class="card main-card background-color">
                <#import "../helpers/globalDatePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate "/transactions"/>

                <@header.content>
                    <div class="container transaction-container">
                        <div class="row">
                            <div class="col s4">
                                <div class="icon-block">
                                    <h1 class="center text-green budget-headline-icon"><i class="material-icons icon-budget">file_download</i></h1>
                                    <h5 class="center budget">${currencyService.getCurrencyString(budget.getIncomeSum())}</h5>
                                    <h5 class="center budget-headline">${locale.getString("title.incomes")}</h5>
                                </div>
                            </div>
                            <div class="col s4">
                                <div class="icon-block">
                                    <h1 class="center ${redTextColor} budget-headline-icon"><i class="material-icons icon-budget">file_upload</i></h1>
                                    <h5 class="center budget">${currencyService.getCurrencyString(budget.getExpenditureSum())}</h5>
                                    <h5 class="center budget-headline">${locale.getString("title.expenditures")}</h5>
                                </div>
                            </div>
                            <div class="col s4">
                                <div class="icon-block">
                                    <h1 class="center text-blue budget-headline-icon"><i class="fas fa-piggy-bank icon-budget"></i></h1>
                                    <h5 class="center budget">${currencyService.getCurrencyString(budget.getRest())}</h5>
                                    <h5 class="center budget-headline">${locale.getString("title.rest")}</h5>
                                </div>
                            </div>
                        </div>

                        <#-- buttons -->
                        <@transactionsMacros.buttons filterConfiguration.isActive()/>

                        <#-- transactions list -->
                        <#assign lastDate=''/>

                        <#list transactions as transaction>
                            <#assign shouldHighlight = highlightID?? && transaction.getID()?? && transaction.getID()==highlightID/>

                            <#assign transactionDate=dateService.getDateStringWithMonthName(transaction.date)?upper_case/>
                            <#if transactionDate != lastDate>
                                <#if !transaction?is_first>
                                    </div> <#-- close "transaction-date-group" div from previous loop iteration -->
                                </#if>
                                <div class="transaction-date-group">
                                    <div class="col s12 center-align bold transaction-text transaction-date">
                                        ${transactionDate}
                                    </div>
                                    <#assign lastDate=transactionDate/>
                            </#if>

                            <div class="hide-on-large-only transaction-row-top card transaction-card background-light <#if transaction.isFuture()>transaction-row-transparent</#if> <#if shouldHighlight>background-blue-light transaction-row-transparent-override" id="highlighted-small"<#else>"</#if>>
                                <div class="row valign-wrapper transaction-row-bottom no-margin-bottom">
                                    <@transactionsMacros.transactionNameAndDescription transaction "s6"/>
                                    <@transactionsMacros.transactionAmount transaction account "s6"/>
                                </div>
                                <div class="row valign-wrapper transaction-row-bottom">
                                    <@transactionsMacros.transactionCategory transaction "left-align no-margin-left"/>
                                    <@transactionsMacros.transactionAccountIcon transaction/>
                                    <@transactionsMacros.transactionType transaction "s2"/>
                                    <@transactionsMacros.transactionButtons transaction "s6 right-align"/>
                                </div>
                            </div>

                            <div class="hide-on-med-and-down transaction-row-top card transaction-card transaction-row-bottom background-light <#if transaction.isFuture()>transaction-row-transparent</#if> <#if shouldHighlight>background-blue-light transaction-row-transparent-override" id="highlighted-large"<#else>"</#if>>
                                <div class="row valign-wrapper no-margin-bottom transaction-row-desktop">
                                    <@transactionsMacros.transactionCategory transaction "left-align"/>
                                    <@transactionsMacros.transactionAccountIcon transaction/>
                                    <@transactionsMacros.transactionType transaction "l1 xl1"/>
                                    <@transactionsMacros.transactionNameAndDescription transaction "l4 xl5"/>
                                    <@transactionsMacros.transactionAmount transaction account "l3 xl3 center-align"/>
                                    <@transactionsMacros.transactionButtons transaction "l2 xl1"/>
                                </div>
                            </div>
                        </#list>

                        <#-- show placeholde text if no transactions are present in the current month or REST ist the only transaction -->
                        <@transactionsMacros.placeholder transactions/>
                    </div>
                </@header.content>
            </div>

            <div id="deleteModalContainerOnDemand"></div>

            <@filterMacros.filterModal filterConfiguration/>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/helpers.js'/>"></script>
        <script src="<@s.url '/js/transactions.js'/>"></script>
        <script src="<@s.url '/js/globalDatePicker.js'/>"></script>
        <script src="<@s.url '/js/filter.js'/>"></script>
        <script>document.cookie = "currentDate=${dateService.getDateStringNormal(currentDate)}";</script>
    </@header.body>
</html>
