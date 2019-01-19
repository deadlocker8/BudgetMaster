<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "categories"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "categories"/>

        <#import "categoriesFunctions.ftl" as categoriesFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.categories")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="<@s.url '/categories/newCategory'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.category.new")}</a></div>
                <br>
                <div class="container">
                    <table class="bordered">
                        <#list categories as category>
                            <#assign categoryName=categoriesFunctions.getCategoryName(category)>
                            <tr>
                            <td>
                                <div class="category-circle" style="background-color: ${category.color}">
                                    <span style="color: ${category.getAppropriateTextColor()}">
                                        ${categoryName?capitalize[0]}
                                    </span>
                                </div>
                            </td>
                            <td>${categoryName}</td>
                            <td>
                                <a href="<@s.url '/categories/${category.ID?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                <#if (category.getType().name() == "CUSTOM")>
                                    <a href="<@s.url '/categories/${category.ID?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
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
                <div id="modalConfirmDelete" class="modal background-color">
                    <div class="modal-content">
                        <h4>${locale.getString("info.title.category.delete")}</h4>
                        <p>${locale.getString("info.text.category.delete", currentCategory.name)}</p>
                    </div>
                    <div class="modal-footer background-color">
                        <a href="<@s.url '/categories'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                        <a href="<@s.url '/categories/${currentCategory.ID?c}/delete'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("delete")}</a>
                    </div>
                </div>
            </#if>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/categories.js'/>"></script>
    </body>
</html>