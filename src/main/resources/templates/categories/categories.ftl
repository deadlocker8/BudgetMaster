<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "categories"/>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../navbar.ftl" as navbar>
        <@navbar.navbar "categories"/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.categories")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="/categories/newCategory" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.category.new")}</a></div>
                <br>
                <div class="container">
                    <table class="bordered">
                        <#list categories as category>
                        <tr>
                            <td>
                                <div class="category-circle" style="background-color: ${category.color}">
                                    <span style="color: ${category.getAppropriateTextColor()}">
                                        ${category.name?capitalize[0]}
                                    </span>
                                </div>
                            </td>
                            <td>${category.name}</td>
                            <td>
                                <a href="/categories/${category.ID}/edit" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                <#if (category.type.name() == "CUSTOM")>
                                    <a href="/categories/${category.ID}/requestDelete" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
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
                        <a href="/categories" class="modal-action modal-close waves-effect waves-red btn-flat ">${locale.getString("cancel")}</a>
                        <a href="/categories/${currentCategory.ID}/delete" class="modal-action modal-close waves-effect waves-green btn-flat ">${locale.getString("delete")}</a>
                    </div>
                </div>
            </#if>
        </main>

        <!--  Scripts-->
        <#import "../scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="/js/categories.js"></script>
    </body>
</html>