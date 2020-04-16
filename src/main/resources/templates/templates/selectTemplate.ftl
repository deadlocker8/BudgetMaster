<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "collapsible"/>
        <@header.style "templates"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "templates" settings/>

        <#import "templateFunctions.ftl" as templateFunctions>
        <#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.templates")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="<@s.url '/templates'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">edit</i>${locale.getString("home.menu.templates.action.manage")}</a></div>
                <br>
                <#if templates?size == 0>
                    <div class="container">
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    </div>
                <#else>
                    <@templateFunctions.listTemplates templates/>
                </#if>
            </div>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/templates.js'/>"></script>
    </body>
</html>