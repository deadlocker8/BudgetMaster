<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.transactions.templates')}"/>
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
                        <div class="headline"><i class="material-icons">file_copy</i> ${locale.getString("menu.transactions.templates")}</div>
                    </div>

                    <@header.content>
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
                    <#assign hint=helpers.getHintByLocalizationKey("hint.template.arrow.keys")/>
                    <@header.hint hint=hint/>
                    <#assign hint2=helpers.getHintByLocalizationKey("hint.template.sort.groups")/>
                    <@header.hint hint=hint2/>
                    <br>
                    <#if templatesByGroup?size == 0>
                        <div class="container">
                            <div class="headline center-align">${locale.getString("placeholder")}</div>
                        </div>
                    <#else>
                        <@templateFunctions.listTemplates templatesByGroup/>
                    </#if>
                </div>
            </@header.content>

            <div id="deleteModalContainerOnDemand"></div>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/webjars/sortablejs/1.15.0/Sortable.min.js'/>"></script>
        <script src="<@s.url '/js/templates.js'/>"></script>
    </@header.body>
</html>