<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.globals/>
		<@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
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

                    <@header.content>
                        <#assign hint=helpers.getHintByLocalizationKey("hint.first.use.teaser")/>
                        <@header.hint hint=hint icon="fas fa-graduation-cap" actionUrl="/firstUse"/>

                        <div class="hide-on-small-only"><br></div>

                        <div class="row home-menu-flex">
                            <@indexFunctions.homeEntry url="/accounts" icon=entityType.ACCOUNT.getIcon() iconColor="" headlineText="menu.accounts" bodyText="home.menu.accounts">
                                <@indexFunctions.action url="/accounts" name="home.menu.accounts.action.manage"/>
                                <@indexFunctions.action url="/accounts/newAccount" name="home.menu.accounts.action.new"/>
                            </@indexFunctions.homeEntry>

                            <@indexFunctions.homeEntry url="/transactions" icon=entityType.TRANSACTION.getIcon() iconColor=entityType.TRANSACTION.getColorAsTextColor() headlineText="menu.transactions" bodyText="home.menu.transactions">
                                <@indexFunctions.action url="/transactions" name="home.menu.transactions.action.manage"/>
                                <@indexFunctions.action url="/transactions/newTransaction/normal" name="home.menu.transactions.action.new"/>
                            </@indexFunctions.homeEntry>

                            <@indexFunctions.homeEntry url="/templates" icon=entityType.TEMPLATE.getIcon() iconColor=entityType.TEMPLATE.getColorAsTextColor() headlineText="menu.transactions.templates" bodyText="home.menu.templates">
                                <@indexFunctions.action url="/templates" name="home.menu.templates.action.manage"/>
                            </@indexFunctions.homeEntry>

                            <@indexFunctions.homeEntry url="/charts" icon=entityType.CHART.getIcon() iconColor=entityType.CHART.getColorAsTextColor() headlineText="menu.charts" bodyText="home.menu.charts">
                                <@indexFunctions.action url="/charts/manage" name="home.menu.charts.action.manage"/>
                                <br>
                                <@indexFunctions.action url="/charts" name="home.menu.charts.action.show"/>
                            </@indexFunctions.homeEntry>

                            <@indexFunctions.homeEntry url="/reports" icon=entityType.REPORT.getIcon() iconColor=entityType.REPORT.getColorAsTextColor() headlineText="menu.reports" bodyText="home.menu.reports">
                                <@indexFunctions.action url="/reports" name="home.menu.reports.action.new"/>
                            </@indexFunctions.homeEntry>

                            <@indexFunctions.homeEntry url="/categories" icon=entityType.CATEGORY.getIcon() iconColor=entityType.CATEGORY.getColorAsTextColor() headlineText="menu.categories" bodyText="home.menu.categories">
                                <@indexFunctions.action url="/categories" name="home.menu.categories.action.manage"/>
                                <@indexFunctions.action url="/categories/newCategory" name="home.menu.categories.action.new"/>
                            </@indexFunctions.homeEntry>
                        </div>
                    </@header.content>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>