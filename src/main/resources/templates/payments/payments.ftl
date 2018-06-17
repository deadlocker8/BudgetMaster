<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <link type="text/css" rel="stylesheet" href="/css/categories.css"/>
        <#assign locale = static["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../navbar.ftl" as navbar>
        <@navbar.navbar "payments"/>

        <main>
            <div class="card main-card">
                <#import "../datePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate "/payments"/>
                <div class="container">
                    <div class="row">
                        <div class="col s4">
                            <div class="icon-block">
                                <h1 class="center text-green budget-headline-icon"><i class="material-icons icon-budget">file_download</i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(incomeSum)}</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">${locale.getString("title.incomes")}</h5>
                            </div>
                        </div>
                        <div class="col s4">
                            <div class="icon-block">
                                <h1 class="center text-red budget-headline-icon"><i class="material-icons icon-budget">file_upload</i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(paymentSum)}</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">${locale.getString("title.payments")}</h5>
                            </div>
                        </div>
                        <div class="col s4">
                            <div class="icon-block">
                                <h1 class="center budgetmaster-blue-text budget-headline-icon"><i class="fas fa-piggy-bank icon-budget"></i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(rest)}</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">${locale.getString("title.rest")}</h5>
                            </div>
                        </div>
                    </div>

                    <#-- button new -->
                    <div class="row valign-wrapper">
                        <div class="col s12 center-align"><a href="/payments/newPayment" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.payment.new")}</a></div>
                    </div>

                    <#-- payments list -->
                    <br>
                    <table class="bordered">
                        <#list payments as payment>
                            <tr>
                                <td>${helpers.getDateString(payment.date)}</td>
                                <td><i class="material-icons">repeat</i></td>
                                <td>
                                    <div class="category-circle" style="background-color: ${payment.category.color}">
                                        <span style="color: ${payment.category.getAppropriateTextColor()}">
                                            ${payment.category.name?capitalize[0]}
                                        </span>
                                    </div>
                                </td>
                                <td>
                                    <div>${payment.name}</div>
                                    <#if payment.description??>
                                        <div class="italic">${payment.description}</div>
                                    </#if>
                                </td>
                                <#if payment.amount <= 0>
                                    <td class="bold text-red">${helpers.getCurrencyString(payment.amount)}</td>
                                <#else>
                                    <td class="bold text-dark-green">${helpers.getCurrencyString(payment.amount)}</td>
                                </#if>
                                <td>
                                    <#if (payment.category.type.name() != "REST")>
                                        <a href="/payments/${payment.ID}/edit" class="btn-flat no-padding"><i class="material-icons left">edit</i></a>
                                        <a href="/payments/${payment.ID}/requestDelete" class="btn-flat no-padding"><i class="material-icons left">delete</i></a>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </table>
                </div>
            </div>

            <#if currentPayment??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal">
                    <div class="modal-content">
                        <h4>${locale.getString("info.title.payment.delete")}</h4>
                        <p>${locale.getString("info.text.payment.delete", currentPayment.name)}</p>
                    </div>
                    <div class="modal-footer">
                        <a href="/payments" class="modal-action modal-close waves-effect waves-red btn-flat ">${locale.getString("cancel")}</a>
                        <a href="/payments/${currentPayment.ID}/delete" class="modal-action modal-close waves-effect waves-green btn-flat ">${locale.getString("delete")}</a>
                    </div>
                </div>
            </#if>
        </main>

        <!--  Scripts-->
        <#import "../scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="/js/payments.js"></script>
        <script src="/js/datePicker.js"></script>
        <script>document.cookie = "currentDate=${helpers.getDateString(currentDate)}";</script>
    </body>
</html>