<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "categories"/>
        <@header.style "globalDatepicker"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "transactions"/>

        <main>
            <div class="card main-card background-color">
                <#import "../helpers/globalDatePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate springMacroRequestContext.getRequestUri()/>
                <div class="container">
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

                    <#-- button new -->
                    <div class="row valign-wrapper">
                        <div class="col s12 center-align"><a href="<@s.url '/transactions/newTransaction'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.transaction.new")}</a></div>
                    </div>

                    <#-- transactions list -->
                    <br>
                    <table class="bordered">
                        <#list transactions as transaction>
                            <tr>
                                <td>${helpers.getDateString(transaction.date)}</td>
                                <td><#if transaction.isRepeating()><i class="material-icons">repeat</i></#if></td>
                                <td>
                                    <div class="category-circle" style="background-color: ${transaction.category.color}">
                                        <span style="color: ${transaction.category.getAppropriateTextColor()}">
                                            ${transaction.category.name?capitalize[0]}
                                        </span>
                                    </div>
                                </td>
                                <td class="transaction-name">
                                    <div>${transaction.name}</div>
                                    <#if transaction.description??>
                                        <div class="italic">${transaction.description}</div>
                                    </#if>
                                </td>
                                <#if transaction.amount <= 0>
                                    <td class="bold ${redTextColor}">${helpers.getCurrencyString(transaction.amount)}</td>
                                <#else>
                                    <td class="bold ${greenTextColor}">${helpers.getCurrencyString(transaction.amount)}</td>
                                </#if>
                                <td>
                                    <#if (transaction.category.type.name() != "REST")>
                                        <a href="<@s.url '/transactions/${transaction.ID?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                        <a href="<@s.url '/transactions/${transaction.ID?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </table>

                    <#-- show placeholde text if no transactions are present in the current month or REST ist the only transaction -->
                    <#assign isOnlyRest = transactions?size == 1 && transactions[0].category.type.name() == "REST"/>
                    <#if isOnlyRest>
                        <br>
                    </#if>

                    <#if transactions?size == 0 || isOnlyRest>
                        <div class="headline center-align">${locale.getString("placeholder.seems.empty")}</div>
                        <div class="headline-advice center-align">${locale.getString("placeholder.advice", locale.getString("menu.transactions"))}</div>
                    </#if>
                </div>
            </div>

            <#if currentTransaction??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal background-color">
                    <div class="modal-content">
                        <h4>${locale.getString("info.title.transaction.delete")}</h4>
                        <#if currentTransaction.isRepeating()>
                            <p>${locale.getString("info.text.transaction.repeating.delete", currentTransaction.name)}</p>
                        <#else>
                            <p>${locale.getString("info.text.transaction.delete", currentTransaction.name)}</p>
                        </#if>
                    </div>
                    <div class="modal-footer background-color">
                        <a href="<@s.url '/transactions'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                        <a href="<@s.url '/transactions/${currentTransaction.ID?c}/delete'/>" class="modal-action modal-close waves-effectwaves-light green btn-flat white-text">${locale.getString("delete")}</a>
                    </div>
                </div>
            </#if>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/transactions.js'/>"></script>
        <script src="<@s.url '/js/globalDatePicker.js'/>"></script>
        <script>document.cookie = "currentDate=${helpers.getDateString(currentDate)}";</script>
    </body>
</html>