<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#assign locale = static["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar active/>

        <!--  Scripts-->
         <#import "scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>