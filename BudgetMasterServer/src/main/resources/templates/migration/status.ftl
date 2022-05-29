<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('title.migration')}"/>
        <#import "/spring.ftl" as s>
        <@header.style "collapsible"/>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "migration" settings/>
        <#import "../helpers/validation.ftl" as validation>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("title.migration")}</div>
                    </div>
                </div>

                <div class="container center-align">
                    <div id="migration-status" data-url="<@s.url '/migration/getStatus'/>"></div>
                </div>
            </div>
        </main>

        <script>
            migrationStatus = "${status.name()}";
        </script>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/migration.js'/>"></script>
    </@header.body>
</html>