<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.tags')}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "tags" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">local_offer</i> ${locale.getString("menu.tags")}</div>
                    </div>
                </div>

                <@header.content>
                    <div class="container">
                        <#if tagUsages?keys?size == 0>
                            <div class="headline center-align">${locale.getString("placeholder")}</div>
                        <#else>
                            <table class="bordered">
                                <thead>
                                    <tr>
                                        <th>${locale.getString("category.new.label.name")}</th>
                                        <th>${locale.getString("categories.usages")}</th>
                                        <th>${locale.getString("categories.actions")}</th>
                                    </tr>
                                </thead>
                                <#list tagUsages as tagName, usageCount>
                                    <tr>
                                        <td>${tagName} </td>
                                        <td>
                                            <a href="<@s.url '/search?searchTags=true&searchText=' + tagName/>" class="waves-effect waves-light text-default">${usageCount}</a>
                                        </td>
                                        <td>
                                            <@header.buttonFlat url='/search?searchTags=true&searchText=' + tagName icon='search' localizationKey='' classes="no-padding text-default"/>
                                        </td>
                                    </tr>
                                </#list>
                            </table>
                        </#if>
                    </div>
                </@header.content>
            </div>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>
