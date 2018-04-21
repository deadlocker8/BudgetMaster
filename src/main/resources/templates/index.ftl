<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header "BudgetMaster"/>
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
                                <h5 class="center budget">2350,15 €</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">Einnahmen</h5>
                            </div>
                        </div>
                        <div class="col s12 m4">
                            <div class="icon-block">
                                <h1 class="center text-red"><i class="material-icons icon-budget">file_upload</i></h1>
                                <h5 class="center budget">-576,33 €</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">Ausgaben</h5>
                            </div>
                        </div>
                        <div class="col s12 m4">
                            <div class="icon-block">
                                <h1 class="center budgetmaster-blue-text"><i class="material-icons icon-budget">account_balance</i></h1>
                                <h5 class="center budget">1773,82 €</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">Rest</h5>
                            </div>
                        </div>
                    </div>
                    <div class="hide-on-small-only"><br><br></div>
                    <div class="row">
                        <div class="col s12">
                            <div class="budget-bar-container">
                                <div class="budget-bar color-green" style="width: 100%"></div>
                            </div>
                            <div class="budget-bar-container">
                                <div class="budget-bar color-red" style="width: 25%"></div>
                            </div>
                            <div class="budget-bar-container">
                                <div class="budget-bar budgetmaster-blue" style="width: 75%"></div>
                            </div>
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