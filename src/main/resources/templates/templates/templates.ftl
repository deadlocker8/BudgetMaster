<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.templates')}"/>
        <@header.style "collapsible"/>
        <@header.style "templates"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "templates" settings/>

        <#import "templateFunctions.ftl" as templateFunctions>
        <#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">file_copy</i> ${locale.getString("menu.templates")}</div>
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
                <@header.modalConfirmDelete title=locale.getString("info.title.template.delete") confirmUrl='/templates' cancelUrlBase='/templates' itemId=currentTemplate.getID() confirmButtonTextKey='info.title.template.delete'>
                    <p>${locale.getString("info.text.template.delete", currentTemplate.getTemplateName())}</p>
                </@header.modalConfirmDelete>
            </#if>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/templates.js'/>"></script>
    </@header.body>
</html>