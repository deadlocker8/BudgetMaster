<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "templates" settings/>

        <#import "templateFunctions.ftl" as templateFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.templates")}</div>
                    </div>
                </div>
                <br>
                <@templateFunctions.buttons/>
                <br>
                <div class="container">
                    <#if templates?size == 0>
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    <#else>
                        <table class="bordered">
                            <thead>
                                <tr>
                                    <th>${locale.getString("chart.new.label.name")}</th>
                                    <th>${locale.getString("chart.actions")}</th>
                                </tr>
                            </thead>
                            <#list templates as template>
                                <tr>
                                    <td>${template.getTemplateName()}</td>
                                    <td>
                                        <a href="<@s.url '/templates/${template.ID?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                        <a href="<@s.url '/templates/${template.ID?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    </#if>
                </div>
            </div>

            <#if currentTemplate??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal background-color">
                    <div class="modal-content">
                        <h4>${locale.getString("info.title.template.delete")}</h4>
                        <p>${locale.getString("info.text.template.delete", currentTemplate.getName())}</p>
                    </div>
                    <div class="modal-footer background-color">
                        <a href="<@s.url '/templates/manage'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
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