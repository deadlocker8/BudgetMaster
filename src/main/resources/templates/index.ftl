<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#assign locale = static["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar "home"/>

        <main>
            <div class="card main-card">
                <#import "datePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate "/"/>
                <div class="hide-on-small-only"><br></div>
                <div class="container">
                    <div class="row">
                        <div class="col s12 m4">
                            <div class="icon-block">
                                <h1 class="center text-green"><i class="material-icons icon-budget">file_download</i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(incomeSum)}</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">${locale.getString("title.incomes")}</h5>
                            </div>
                        </div>
                        <div class="col s12 m4">
                            <div class="icon-block">
                                <h1 class="center text-red"><i class="material-icons icon-budget">file_upload</i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(paymentSum)}</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">${locale.getString("title.payments")}</h5>
                            </div>
                        </div>
                        <div class="col s12 m4">
                            <div class="icon-block">
                                <h1 class="center budgetmaster-blue-text"><i class="fas fa-piggy-bank icon-budget"></i></h1>
                                <h5 class="center budget">${helpers.getCurrencyString(rest)}</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">${locale.getString("title.rest")}</h5>
                            </div>
                        </div>
                    </div>
                    <div class="hide-on-small-only"><br><br></div>
                    <div class="row">
                        <#if incomeSum gt paymentSum?abs>
                            <#assign fullPercentage = incomeSum>
                        <#else>
                            <#assign fullPercentage = paymentSum?abs>
                        </#if>

                        <div class="col s12">
                            <#if fullPercentage gt 0>
                                <div class="budget-bar-container">
                                    <div class="budget-bar color-green" style="width: ${(100 / fullPercentage * incomeSum)?abs?c}%"></div>
                                </div>
                            </#if>
                            <#if fullPercentage gt 0>
                                <div class="budget-bar-container">
                                    <div class="budget-bar color-red" style="width: ${(100 / fullPercentage * paymentSum)?abs?c}%"></div>
                                </div>
                            </#if>
                            <#if rest gt 0>
                                <div class="budget-bar-container">
                                    <div class="budget-bar budgetmaster-blue" style="width: ${(100 / fullPercentage * rest)?abs?c}%"></div>
                                </div>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="/js/datePicker.js"></script>
    </body>
</html>