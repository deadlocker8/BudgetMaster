<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "reports"/>

        <main>
            <div class="card main-card background-color">
                <#import "../helpers/datePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate springMacroRequestContext.getRequestUri()/>
                <div class="container">
                    <div class="row">
                        <div class="col s4">

                        </div>
                    </div>

                    <#-- button new -->
                    <div class="row valign-wrapper">
                        <div class="col s12 center-align"><a href="<@s.url '/reports/generate'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">save</i>${locale.getString("report.generate")}</a></div>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/datePicker.js'/>"></script>
        <script>document.cookie = "currentDate=${helpers.getDateString(currentDate)}";</script>
    </body>
</html>