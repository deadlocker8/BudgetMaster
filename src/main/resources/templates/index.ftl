<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "home" settings/>

        <#import "indexFunctions.ftl" as indexFunctions>

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

                    <#if settings.getShowFirstUseBanner()>
                        <@indexFunctions.firstUseBanner/>
                    </#if>

                    <div class="hide-on-small-only"><br></div>

                    <div class="row home-menu-flex">
                        <@indexFunctions.homeEntry url="/accounts" icon="account_balance" iconColor="" headlineText="menu.accounts" bodyText="home.menu.accounts">
                            <@indexFunctions.action url="/accounts" name="home.menu.accounts.action.manage"/>
                            <@indexFunctions.action url="/accounts/newAccount" name="home.menu.accounts.action.new"/>
                        </@indexFunctions.homeEntry>

                        <@indexFunctions.homeEntry url="/transactions" icon="list" iconColor="budgetmaster-baby-blue-text" headlineText="menu.transactions" bodyText="home.menu.transactions">
                            <@indexFunctions.action url="/transactions" name="home.menu.transactions.action.manage"/>
                            <@indexFunctions.action url="/transactions/newTransaction/normal" name="home.menu.transactions.action.new"/>
                        </@indexFunctions.homeEntry>

                        <@indexFunctions.homeEntry url="/templates" icon="file_copy" iconColor="budgetmaster-dark-orange-text" headlineText="menu.templates" bodyText="home.menu.templates">
                            <@indexFunctions.action url="/templates" name="home.menu.templates.action.manage"/>
                        </@indexFunctions.homeEntry>

                        <@indexFunctions.homeEntry url="/charts" icon="show_chart" iconColor="budgetmaster-purple-text" headlineText="menu.charts" bodyText="home.menu.charts">
                            <@indexFunctions.action url="/charts/manage" name="home.menu.charts.action.manage"/>
                            <br>
                            <@indexFunctions.action url="/charts" name="home.menu.charts.action.show"/>
                        </@indexFunctions.homeEntry>

                        <@indexFunctions.homeEntry url="/reports" icon="description" iconColor="budgetmaster-green-text" headlineText="menu.reports" bodyText="home.menu.reports">
                            <@indexFunctions.action url="/reports" name="home.menu.reports.action.new"/>
                        </@indexFunctions.homeEntry>

                        <@indexFunctions.homeEntry url="/categories" icon="label" iconColor="budgetmaster-orange-text" headlineText="menu.categories" bodyText="home.menu.categories">
                            <@indexFunctions.action url="/categories" name="home.menu.categories.action.manage"/>
                            <@indexFunctions.action url="/categories/newCategory" name="home.menu.categories.action.new"/>
                        </@indexFunctions.homeEntry>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>