<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
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
                    <div class="row">
                        <div class="input-field col s12 m12 l8 offset-l2">
                            <i class="material-icons prefix">search</i>
                            <input id="searchTemplate" type="text" class="mousetrap">
                            <label for="searchTemplate">${locale.getString("search")}</label>
                        </div>
                    </div>
                </div>
                <br>
                <@templateFunctions.buttons/>
                <br>
                <#if templates?size == 0>
                    <div class="container">
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    </div>
                <#else>
                    <@templateFunctions.listTemplates templates/>
                </#if>
            </div>

            <#if currentTemplate??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal background-color">
                    <div class="modal-content">
                        <h4>${locale.getString("info.title.template.delete")}</h4>
                        <p>${locale.getString("info.text.template.delete", currentTemplate.getTemplateName())}</p>
                    </div>
                    <div class="modal-footer background-color">
                        <a href="<@s.url '/templates'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                        <a href="<@s.url '/templates/${currentTemplate.getID()?c}/delete'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("info.title.template.delete")}</a>
                    </div>
                </div>
            </#if>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/templates.js'/>"></script>
    </body>
</html>