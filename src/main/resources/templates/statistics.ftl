<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.firstUseGuide')}"/>
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

                    <div class="container">
                        <div class="container center-align">
                            <div class="row left-align">
                                <div class="col s12">
                                    Lorem Ipsum
                                </div>
                            </div>
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
