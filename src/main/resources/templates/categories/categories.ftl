<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.categories')}"/>
        <@header.style "categories"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "categories" settings/>

        <#import "categoriesFunctions.ftl" as categoriesFunctions>
        <#import "../transactions/newTransactionMacros.ftl" as newTransactionMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">label</i> ${locale.getString("menu.categories")}
                        </div>
                    </div>
                </div>

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
                                <@categoryCircle category categoryName/>
                                <td>${categoryName} </td>
                                <td>${usageCount}</td>
                                <td>
                                    <a href="<@s.url '/categories/${category.ID?c}/edit'/>" class="btn-flat no-padding text-default"><i class="material-icons left">edit</i></a>
                                    <#if (category.getType().name() == "CUSTOM")>
                                        <a href="<@s.url '/categories/${category.ID?c}/requestDelete'/>" class="btn-flat no-padding text-default"><i class="material-icons left no-margin">delete</i></a>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </table>
                    <#if categories?size == 0>
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    </#if>
                </div>
            </div>

            <#if currentCategory??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal categoryDeleteModal background-color">
                    <div class="modal-content">
                        <h4>${locale.getString("info.title.category.delete")}</h4>
                        <p>${locale.getString("info.text.category.delete", currentCategory.name)}</p>
                        <p>${locale.getString("info.text.category.delete.move")}</p>

                        <form name="DestinationCategory" id="formDestinationCategory" action="<@s.url '/categories/${currentCategory.ID?c}/delete'/>" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <#import "../helpers/validation.ftl" as validation>
                            <@newTransactionMacros.categorySelect availableCategories preselectedCategory "col s12 m12 l8 offset-l2" locale.getString("info.title.category.delete.move")/>
                        </form>
                    </div>

                    <div class="modal-footer background-color">
                        <@header.buttonLink url='/categories' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white'/>
                        <@header.buttonLink url='' icon='delete' localizationKey='delete' color='green' id='buttonDeleteCategory' classes='modal-action modal-close text-white' noUrl=true/>
                    </div>
                </div>
            </#if>
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
        <script src="<@s.url '/js/categorySelect.js'/>"></script>
    </@header.body>
</html>


<#macro categoryCircle category categoryName>
    <td>
        <div class="category-circle" style="background-color: ${category.color}">
            <span style="color: ${category.getAppropriateTextColor()}">
                ${categoryName?capitalize[0]}
            </span>
        </div>
    </td>
</#macro>