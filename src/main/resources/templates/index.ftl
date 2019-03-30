<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "home"/>
        <@navbar.backupReminder settings/>

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
                        <div class="col s12 m6 l4 home-menu-cell">
                            <a href="<@s.url '/accounts'/>" class="home-menu-link btn-flat budget">
                                <i class="material-icons icon-budget left">account_balance</i>
                                ${locale.getString("menu.accounts")}
                            </a>
                            <p class="text-grey home-menu-text">${locale.getString("home.menu.accounts")}</p>
                            <div class="left-align">
                                <a href="<@s.url '/accounts'/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString("home.menu.accounts.action.manage")}</a>
                                <a href="<@s.url '/accounts/newAccount'/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString("home.menu.accounts.action.new")}</a>
                            </div>
                        </div>
                        <div class="col s12 m6 l4 home-menu-cell">
                            <a href="<@s.url '/transactions'/>" class="home-menu-link btn-flat budget">
                                <i class="material-icons icon-budget left budgetmaster-baby-blue-text">list</i>
                                ${locale.getString("menu.transactions")}
                            </a>
                            <p class="text-grey home-menu-text">${locale.getString("home.menu.transactions")}</p>
                            <div class="left-align">
                                <a href="<@s.url '/transactions'/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString("home.menu.transactions.action.manage")}</a>
                                <a href="<@s.url '/transactions/newTransaction'/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString("home.menu.transactions.action.new")}n</a>
                            </div>
                        </div>
                <div class="hide-on-small-only hide-on-large-only">
                    </div>
                    <div class="row home-menu-flex">
                </div>
                        <div class="col s12 m6 l4 home-menu-cell home-menu-disabled">
                            <a class="home-menu-link btn-flat budget">
                                <i class="material-icons icon-budget left budgetmaster-purple-text">show_chart</i>
                                ${locale.getString("menu.charts")}
                            </a>
                            <p class="home-menu-text">${locale.getString("home.menu.charts")}</p>
                        </div>
                <div class="hide-on-med-only">
                    </div>
                    <div class="row home-menu-flex">
                </div>
                        <div class="col s12 m6 l4 home-menu-cell">
                            <a href="<@s.url '/reports'/>" class="home-menu-link btn-flat budget">
                                <i class="material-icons icon-budget left budgetmaster-green-text">description</i>
                                ${locale.getString("menu.reports")}
                            </a>
                            <p class="text-grey home-menu-text">${locale.getString("home.menu.reports")}</p>
                            <div class="left-align">
                                <a href="<@s.url '/reports'/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString("home.menu.reports.action.new")}</a>
                            </div>
                        </div>
                <div class="hide-on-small-only hide-on-large-only">
                    </div>
                    <div class="row home-menu-flex">
                </div>
                        <div class="col s12 m6 l4 home-menu-cell">
                            <a href="<@s.url '/categories'/>" class="home-menu-link btn-flat budget">
                                <i class="material-icons icon-budget left budgetmaster-orange-text">label</i>
                                ${locale.getString("menu.categories")}
                            </a>
                            <p class="text-grey home-menu-text">${locale.getString("home.menu.categories")}</p>
                            <div class="left-align">
                                <a href="<@s.url '/categories'/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString("home.menu.categories.action.manage")}</a>
                                <a href="<@s.url '/categories/newCategory'/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString("home.menu.categories.action.new")}</a>
                            </div>
                        </div>
                        <div class="col s12 m6 l4 home-menu-cell">
                            <a href="<@s.url '/settings'/>" class="home-menu-link btn-flat budget">
                                <i class="material-icons icon-budget left budgetmaster-red-text">settings</i>
                                ${locale.getString("menu.settings")}
                            </a>
                            <p class="text-grey home-menu-text">${locale.getString("home.menu.settings")}</p>
                            <div class="left-align">
                                <a href="<@s.url '/settings'/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString("home.menu.settings.action.manage")}</a>
                            </div>
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