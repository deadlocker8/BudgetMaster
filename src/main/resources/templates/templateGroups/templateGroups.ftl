<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.template.groups')}"/>
        <@header.style "collapsible"/>
        <@header.style "templates"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "templates" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">file_copy</i> ${locale.getString("menu.template.groups")}</div>
                    </div>
                </div>

                <@header.content>
                    <br>
                    <div class="center-align"><@header.buttonLink url='/templateGroups/newTemplateGroup' icon='add' localizationKey='title.template.group.new'/></div>
                    <br>

                    <div class="container">
                        <table class="bordered">
                            <#list templateGroups as group>
                                <tr>
                                    <td>${group.getName()} </td>
                                    <td>
                                        <@header.buttonFlat url='/templateGroups/' + group.ID?c + '/edit' icon='edit' localizationKey='' classes="no-padding text-default"/>
                                        <@header.buttonFlat url='/templateGroups/' + group.ID?c + '/requestDelete' icon='delete' localizationKey='' classes="no-padding text-default button-request-delete-template-group" isDataUrl=true/>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                        <#if templateGroups?size == 0>
                            <div class="headline center-align">${locale.getString("placeholder")}</div>
                        </#if>
                    </div>
                </@header.content>

            <div id="deleteModalContainerOnDemand"></div>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/templateGroups.js'/>"></script>
    </@header.body>
</html>