<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.categories')}"/>
        <@header.style "customSelect"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "categories" settings/>

        <#import "categoriesFunctions.ftl" as categoriesFunctions>
        <#import "../transactions/newTransactionMacros.ftl" as newTransactionMacros>
        <#import "../helpers/customSelectMacros.ftl" as customSelectMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">label</i> ${locale.getString("menu.categories")}
                        </div>
                    </div>
                </div>

                <@header.content>
                    <br>
                    <div class="center-align"><@header.buttonLink url='/categories/newCategory' icon='add' localizationKey='title.category.new'/></div>
                    <br>

                    <div class="container">
                    <table class="bordered">
                        <thead>
                            <tr>
                                <th></th>
                                <th>${locale.getString("category.new.label.name")}</th>
                                <th>${locale.getString("categories.usages")}</th>
                                <th>${locale.getString("categories.actions")}</th>
                            </tr>
                        </thead>
                        <#list categories as category>
                            <#assign categoryName=categoriesFunctions.getCategoryName(category)>
                            <#assign usageCount=helpers.getUsageCountForCategory(category)/>
                            <tr>
                                <td><@categoriesFunctions.categoryCircle category=category enableSearchWrapper=true/></td>
                                <td>${categoryName} </td>
                                <td><a href="<@s.url '/search?searchCategory=true&searchText=' + categoryName/>" class="waves-effect waves-light text-default">${usageCount}</a></td>
                                <td>
                                    <@header.buttonFlat url='/categories/' + category.ID?c + '/edit' icon='edit' localizationKey='' classes="no-padding text-default"/>
                                    <@header.buttonFlat url='/search?searchCategory=true&searchText=' + categoryName icon='search' localizationKey='' classes="no-padding text-default"/>
                                    <#if (category.getType().name() == "CUSTOM")>
                                        <@header.buttonFlat url='/categories/' + category.ID?c + '/requestDelete' icon='delete' localizationKey='' classes="no-padding text-default button-request-delete-category" isDataUrl=true/>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </table>
                    <#if categories?size == 0>
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    </#if>
                </div>
                </@header.content>
            </div>

            <div id="deleteModalContainerOnDemand"></div>
        </main>

        <!--  Scripts-->
        <#-- pass selected account to JS in order to select current value for materialize select -->
        <script>
            <#if preselectedCategory??>
            selectedCategory = "${preselectedCategory.getID()?c}";
            </#if>
        </script>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/categories.js'/>"></script>
    </@header.body>
</html>
