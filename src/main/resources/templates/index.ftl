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
                <div class="container">
                <#-- icon -->
                    <div class="row">
                        <div class="col s8 offset-s2 center-align">
                            <a href="/about">
                                <img class="responsive-img" id="logo-home" src="/images/Logo_with_text_medium_res.png">
                            </a>
                        </div>
                    </div>
                    <div class="hide-on-small-only"><br></div>

                    <div class="row home-menu-flex">
                        <div class="col s12 m4 center-align home-menu-cell">
                            <a href="/accounts" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-grey-text"><i class="material-icons icon-budget">account_balance</i></h1>
                                <h3 class="center budget">${locale.getString("menu.accounts")}</h3>
                                <p class="grey-text text-darken-1">Accounts allow you to group multiple payments. You can create as many accounts as you want. Create, update and delete them by clicking here.</p>
                            </a>
                        </div>
                        <div class="col s12 m4 center-align home-menu-cell">
                            <a href="/payments" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-baby-blue-text"><i class="material-icons icon-budget">list</i></h1>
                                <h3 class="center budget">${locale.getString("menu.payments")}</h3>
                                <p class="grey-text text-darken-1">Payments are the key element of BudgetMaster. Give them a name and description, assign a category and account to them, configure them as recurring, and mark them with tags. You can create, edit and delete them by clicking here.</p>
                            </a>
                        </div>
                        <div class="col s12 m4 center-align home-menu-cell">
                            <a class="home-menu-link budgetmaster-text-black home-menu-disabled">
                                <h1 class="center budgetmaster-purple-text"><i class="material-icons icon-budget">show_chart</i></h1>
                                <h3 class="center budget">${locale.getString("menu.charts")}</h3>
                                <p class="grey-text text-darken-1">This feature will be available in an upcoming release.</p>
                            </a>
                        </div>
                    </div>
                    <div class="row home-menu-flex">
                        <div class="col s12 m4 center-align home-menu-cell">
                            <a class="home-menu-link budgetmaster-text-black home-menu-disabled">
                                <h1 class="center budgetmaster-green-text"><i class="material-icons icon-budget">description</i></h1>
                                <h3 class="center budget">${locale.getString("menu.reports")}</h3>
                                <p class="grey-text text-darken-1">This feature will be available in an upcoming release.</p>
                            </a>
                        </div>
                        <div class="col s12 m4 center-align home-menu-cell">
                            <a href="/categories" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-orange-text"><i class="material-icons icon-budget">label</i></h1>
                                <h3 class="center budget">${locale.getString("menu.categories")}</h3>
                                <p class="grey-text text-darken-1">Categories can be assigned to payments in order to mark them as belonging together. They consists of a name an a color. Latter can be chosen from a predefined color palette or by using a custom color picker. Create, update and delete categories by clicking here.</p>
                            </a>
                        </div>
                        <div class="col s12 m4 center-align home-menu-cell">
                            <a href="/settings" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-red-text"><i class="material-icons icon-budget">settings</i></h1>
                                <h3 class="center budget">${locale.getString("menu.settings")}</h3>
                                <p class="grey-text text-darken-1">Manage general settings such as login password, your preferred language and how to handle updates. This section also offers the possibility to export or delete your data or importing an existing database.</p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>