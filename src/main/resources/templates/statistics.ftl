<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.statistics')}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "statistics" settings/>

        <#import "indexFunctions.ftl" as indexFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">insert_chart</i> ${locale.getString("menu.statistics")}</div>
                    </div>
                </div>

                <@header.content>
                    <br>

                    <div class="container center-align">
                        <div class="row left-align">
                            <#list statisticItems as item>
                                <div class="col s12 m6 xl4 statistics-item">
                                    <div class="card-panel ${item.getBackgroundColor()} ${item.getTextColor()} center-align">
                                        <i class="material-icons">${item.getIcon()}</i>
                                        <div>${item.getText()}</div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </div>
                </@header.content>
            </div>
        </main>

        <!--  Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>
