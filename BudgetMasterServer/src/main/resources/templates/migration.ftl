<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('title.migration')}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "migration" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("title.migration")}</div>
                    </div>
                </div>

                <div class="container">
                    <form name="Import" action="<@s.url '/migration/start'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="row">
                            <div class="col s12 m10 offset-m1 l10 offset-l1 xl8 offset-xl2">
                                <table class="bordered">
                                </table>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col m6 l4 offset-l2 right-align">
                                <@header.buttonLink url='/' icon='clear' localizationKey='cancel' color='red'/>
                            </div>

                            <div class="col m6 l4 left-align">
                                <@header.buttonSubmit name='action' icon='merge' localizationKey='info.button.migration' id='buttonMigrate' color='green'/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!-- Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>