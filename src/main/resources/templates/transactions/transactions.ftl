<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "categories"/>
        <@header.style "transactions"/>
        <@header.style "globalDatepicker"/>
        <@header.style "filter"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "transactions" settings/>

        <#import "transactionsMacros.ftl" as transactionsMacros>
        <#import "../filter/filterMacros.ftl" as filterMacros>

        <main>
            <div class="card main-card background-color">
                <#import "../helpers/globalDatePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate "/transactions"/>
                <div class="container transaction-container">
                    <div class="row">
                        <div class="col s4">
                            <div class="icon-block">
                                <h1 class="center text-green budget-headline-icon"><i class="material-icons icon-budget">file_download</i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(budget.getIncomeSum())}</h5>
                                <h5 class="center budget-headline">${locale.getString("title.incomes")}</h5>
                            </div>
                        </div>
                        <div class="col s4">
                            <div class="icon-block">
                                <h1 class="center ${redTextColor} budget-headline-icon"><i class="material-icons icon-budget">file_upload</i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(budget.getExpenditureSum())}</h5>
                                <h5 class="center budget-headline">${locale.getString("title.expenditures")}</h5>
                            </div>
                        </div>
                        <div class="col s4">
                            <div class="icon-block">
                                <h1 class="center budgetmaster-blue-text budget-headline-icon"><i class="fas fa-piggy-bank icon-budget"></i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(budget.getRest())}</h5>
                                <h5 class="center budget-headline">${locale.getString("title.rest")}</h5>
                            </div>
                        </div>
                    </div>

                    <#-- buttons -->
                    <@transactionsMacros.buttons filterConfiguration.isActive()/>

                    <#-- transactions list -->
                    <br>
                    <#list transactions as transaction>
                        <#assign shouldHighlight = highlightID?? && transaction.getID()?? && transaction.getID()==highlightID/>

                        <div class="hide-on-large-only transaction-row-top <#if shouldHighlight>budgetmaster-blue-light</#if>">
                            <div class="row valign-wrapper transaction-row-bottom">
                                <div class="col s3 center-align bold transaction-text">
                                    ${dateService.getDateStringWithoutYear(transaction.date)}
                                </div>
                                <@transactionsMacros.transactionType transaction/>
                                <@transactionsMacros.transactionButtons transaction/>
                            </div>
                            <div class="row valign-wrapper transaction-row-bottom">
                                <@transactionsMacros.transactionCategory transaction "center-align"/>
                                <@transactionsMacros.transactionNameAndDescription transaction "s5"/>
                                <@transactionsMacros.transactionAmount transaction account "s4"/>
                            </div>
                        </div>
                        <div class="hide-on-med-and-down transaction-row-top transaction-row-bottom <#if shouldHighlight>budgetmaster-blue-light</#if>">
                            <div class="row valign-wrapper no-margin-bottom">
                                <div class="col l1 xl1 bold transaction-text transaction-line-height">
                                    ${dateService.getDateStringWithoutYear(transaction.date)}
                                </div>
                                <@transactionsMacros.transactionCategory transaction "left-align"/>
                                <@transactionsMacros.transactionType transaction/>
                                <@transactionsMacros.transactionNameAndDescription transaction "l4 xl5"/>
                                <@transactionsMacros.transactionAmount transaction account "l3 xl3"/>
                                <@transactionsMacros.transactionButtons transaction/>
                            </div>
                        </div>
                        <hr>
                    </#list>

                    <#-- show placeholde text if no transactions are present in the current month or REST ist the only transaction -->
                    <@transactionsMacros.placeholder transactions/>
                </div>
            </div>

            <#if currentTransaction??>
                <@transactionsMacros.deleteModal currentTransaction/>
            </#if>

            <@filterMacros.filterModal filterConfiguration/>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/transactions.js'/>"></script>
        <script src="<@s.url '/js/globalDatePicker.js'/>"></script>
        <script src="<@s.url '/js/filter.js'/>"></script>
        <script>document.cookie = "currentDate=${dateService.getDateStringNormal(currentDate)}";</script>
    </body>
</html>
