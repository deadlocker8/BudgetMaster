<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.categories')}"/>
        <@header.style "categories"/>
        <@header.style "categorySelect"/>
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
                                <td><@categoriesFunctions.categoryCircle category/></td>
                                <td>${categoryName} </td>
                                <td>${usageCount}</td>
                                <td>
                                    <@header.buttonFlat url='/categories/' + category.ID?c + '/edit' icon='edit' localizationKey='' classes="no-padding text-default"/>
                                    <#if (category.getType().name() == "CUSTOM")>
                                        <@header.buttonFlat url='/categories/' + category.ID?c + '/requestDelete' icon='delete' localizationKey='' classes="no-padding text-default"/>
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
                            <@customSelectMacros.customSelect availableCategories preselectedCategory "col s12 m12 l8 offset-l2" locale.getString("info.title.category.delete.move")/>
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
        <script src="<@s.url '/js/customSelect.js'/>"></script>
    </@header.body>
</html>
