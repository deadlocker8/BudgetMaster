<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
		<@header.header "BudgetMaster"/>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar active settings/>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>