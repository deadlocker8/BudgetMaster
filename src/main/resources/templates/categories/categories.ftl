<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header/>
        <link type="text/css" rel="stylesheet" href="/css/categories.css"/>
        <#assign locale = localization["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../navbar.ftl" as navbar>
        <@navbar.navbar "categories"/>

        <main>
            <div class="card main-card">
                <div class="container">
                    <div class="section center-align">
                        <div class="grey-text text-darken-4 headline">${locale.getString("menu.categories")}</div>
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
                                <a href="/categories/${category.ID}/edit" class="btn-flat no-padding"><i class="material-icons left">edit</i></a>
                                <#if (category.type.name() == "CUSTOM")>
                                    <a href="/categories/${category.ID}/requestDelete" class="btn-flat no-padding"><i class="material-icons left">delete</i></a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                    </table>
                </div>
            </div>

            <#if currentCategory??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal">
                    <div class="modal-content">
                        <h4>Kategorie löschen</h4>
                        <p>Möchtest du die Kategorie "${currentCategory.name}" wirklich löschen?</p>
                    </div>
                    <div class="modal-footer">
                        <a href="/categories" class="modal-action modal-close waves-effect waves-red btn-flat ">Abbrechen</a>
                        <a href="/categories/${currentCategory.ID}/delete" class="modal-action modal-close waves-effect waves-green btn-flat ">Löschen</a>
                    </div>
                </div>
            </#if>
        </main>

        <!--  Scripts-->
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
        <script src="/js/main.js"></script>
        <script src="/js/categories.js"></script>
    </body>
</html>