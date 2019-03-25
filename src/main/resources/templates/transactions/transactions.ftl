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
        <@navbar.navbar "transactions"/>

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
                                <h5 class="center budget">${helpers.getCurrencyString(incomeSum)}</h5>
                                <h5 class="center budget-headline">${locale.getString("title.incomes")}</h5>
                            </div>
                        </div>
                        <div class="col s4">
                            <div class="icon-block">
                                <h1 class="center ${redTextColor} budget-headline-icon"><i class="material-icons icon-budget">file_upload</i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(paymentSum)}</h5>
                                <h5 class="center budget-headline">${locale.getString("title.expenditures")}</h5>
                            </div>
                        </div>
                        <div class="col s4">
                            <div class="icon-block">
                                <h1 class="center budgetmaster-blue-text budget-headline-icon"><i class="fas fa-piggy-bank icon-budget"></i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(rest)}</h5>
                                <h5 class="center budget-headline">${locale.getString("title.rest")}</h5>
                            </div>
                        </div>
                    </div>

                    <#-- buttons -->
                    <@transactionsMacros.buttons filterConfiguration.isActive()/>

                    <#-- transactions list -->
                    <br>
                    <#list transactions as transaction>
                        <div class="hide-on-large-only">
                            <div class="row valign-wrapper transaction-row">
                                <div class="col s3 center-align bold transaction-text">
                                    ${helpers.getDateStringWithoutYear(transaction.date)}
                                </div>
                                <@transactionsMacros.transactionRepeating transaction/>
                                <@transactionsMacros.transactionButtons transaction/>
                            </div>
                            <div class="row valign-wrapper">
                                <@transactionsMacros.transactionCategory transaction "center-align"/>
                                <@transactionsMacros.transactionNameAndDescription transaction/>
                                <@transactionsMacros.transactionAmount transaction.getAmount()/>
                            </div>
                        </div>
                        <div class="hide-on-med-and-down">
                            <div class="row valign-wrapper transaction-row">
                                <div class="col l1 xl1 bold transaction-text">
                                    ${helpers.getDateStringWithoutYear(transaction.date)}
                                </div>
                                <@transactionsMacros.transactionCategory transaction "left-align"/>
                                <@transactionsMacros.transactionRepeating transaction/>
                                <@transactionsMacros.transactionNameAndDescription transaction/>
                                <@transactionsMacros.transactionAmount transaction.getAmount()/>
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
        <script>document.cookie = "currentDate=${helpers.getDateString(currentDate)}";</script>
    </body>
</html>
