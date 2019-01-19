<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "home"/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <#-- icon -->
                    <div class="row">
                        <div class="col s8 offset-s2 center-align">
                            <a href="<@s.url '/about'/>">
                                <@header.logo "logo-home" "responsive-img"/>
                            </a>
                        </div>
                    </div>
                    <div class="hide-on-small-only"><br></div>

                    <div class="row home-menu-flex">
                        <div class="col s12 m6 l4 center-align home-menu-cell">
                            <a href="<@s.url '/accounts'/>" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-grey-text"><i class="material-icons icon-budget">account_balance</i></h1>
                                <h3 class="center budget">${locale.getString("menu.accounts")}</h3>
                                <p class="text-grey">${locale.getString("home.menu.accounts")}</p>
                            </a>
                        </div>
                        <div class="col s12 m6 l4 center-align home-menu-cell">
                            <a href="<@s.url '/transactions'/>" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-baby-blue-text"><i class="material-icons icon-budget">list</i></h1>
                                <h3 class="center budget">${locale.getString("menu.transactions")}</h3>
                                <p class="text-grey">${locale.getString("home.menu.transactions")}</p>
                            </a>
                        </div>
                <div class="hide-on-small-only hide-on-large-only">
                    </div>
                    <div class="row home-menu-flex">
                </div>
                        <div class="col s12 m6 l4 center-align home-menu-cell">
                            <a class="home-menu-link budgetmaster-text-black home-menu-disabled">
                                <h1 class="center budgetmaster-purple-text"><i class="material-icons icon-budget">show_chart</i></h1>
                                <h3 class="center budget">${locale.getString("menu.charts")}</h3>
                                <p class="home-menu-disabled ">${locale.getString("home.menu.charts")}</p>
                            </a>
                        </div>
                <div class="hide-on-med-only">
                    </div>
                    <div class="row home-menu-flex">
                </div>
                        <div class="col s12 m6 l4 center-align home-menu-cell">
                            <a href="<@s.url '/reports'/>" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-green-text"><i class="material-icons icon-budget">description</i></h1>
                                <h3 class="center budget">${locale.getString("menu.reports")}</h3>
                                <p class="text-grey">${locale.getString("home.menu.reports")}</p>
                            </a>
                        </div>
                <div class="hide-on-small-only hide-on-large-only">
                    </div>
                    <div class="row home-menu-flex">
                </div>
                        <div class="col s12 m6 l4 center-align home-menu-cell">
                            <a href="<@s.url '/categories'/>" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-orange-text"><i class="material-icons icon-budget">label</i></h1>
                                <h3 class="center budget">${locale.getString("menu.categories")}</h3>
                                <p class="text-grey">${locale.getString("home.menu.categories")}</p>
                            </a>
                        </div>
                        <div class="col s12 m6 l4 center-align home-menu-cell">
                            <a href="<@s.url '/settings'/>" class="home-menu-link budgetmaster-text-black">
                                <h1 class="center budgetmaster-red-text"><i class="material-icons icon-budget">settings</i></h1>
                                <h3 class="center budget">${locale.getString("menu.settings")}</h3>
                                <p class="text-grey">${locale.getString("home.menu.settings")}</p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>