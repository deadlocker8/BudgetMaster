<html>
    <head>
        <title>BudgetMaster</title>
        <meta charset="UTF-8"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.css">
        <link type="text/css" rel="stylesheet" href="/css/main.css"/>
        <link type="text/css" rel="stylesheet" href="/css/style.css"/>
        <link type="text/css" rel="stylesheet" href="/css/categories.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    </head>
    <body class="budgetmaster-blue-light">
        <ul id="slide-out" class="side-nav fixed">
            <li><a href="/" class="waves-effect" id="nav-logo-container"><img id="nav-logo" src="/images/Logo_with_text.png"></a></li>
            <li><div class="divider"></div></li>
            <li><a href="/" class="waves-effect"><i class="material-icons">home</i>Startseite</a></li>
            <li><a href="#!" class="waves-effect"><i class="material-icons">list</i>Buchungen</a></li>
            <li>
                <ul class="collapsible collapsible-accordion no-padding">
                    <li>
                        <a class="collapsible-header nav-padding"><i class="material-icons">show_chart</i>Diagramme</a>
                        <div class="collapsible-body">
                            <ul>
                                <li><a href="#!" class="waves-effect"><span class="nav-margin">Eingaben/Ausgaben nach Kategorien</span></a></li>
                                <li><a href="#!" class="waves-effect"><span class="nav-margin">Eingaben/Ausgaben pro Monatn</span></a></li>
                                <li><a href="#!" class="waves-effect"><span class="nav-margin">Eingaben/Ausgaben nach Tagsn</span></a></li>
                                <li><a href="#!" class="waves-effect"><span class="nav-margin">Verbrauch nach Kategorienn</span></a></li>
                                <li><a href="#!" class="waves-effect"><span class="nav-margin">Histogrammn</span></a></li>
                            </ul>
                        </div>
                    </li>
                </ul>
            </li>
            <li><a href="#!" class="waves-effect"><i class="material-icons">description</i>Berichte</a></li>
            <li class="active"><a href="/categories" class="waves-effect"><i class="material-icons">label</i>Kategorien</a></li>
            <li><a href="#!" class="waves-effect"><i class="material-icons">settings</i>Einstellungen</a></li>
            <li><div class="divider no-margin"></div></li>
            <li><a href="#!" class="waves-effect"><i class="material-icons">info</i>Über</a></li>
            <li><div class="divider no-margin"></div></li>
            <li><a href="#!" class="waves-effect"><i class="material-icons">lock</i>Logout</a></li>
        </ul>
        <a href="#" data-activates="slide-out" id="mobile-menu" class="mobile-menu"><i class="material-icons left mobile-menu-icon">menu</i>Menü</a>
        <div class="hide-on-large-only"><br></div>

        <main>
            <div class="card main-card">
                <div class="container">
                    <div class="section center-align">
                        <div class="grey-text text-darken-4 headline">Kategorien</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="/categories/add" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>Neue Kategorie</a></div>
                <br>
                <div class="container">
                    <table class="bordered">
                        <#list model["categories"] as category>
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

            <#if model["currentCategory"]??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal">
                    <div class="modal-content">
                        <h4>Kategorie löschen</h4>
                        <p>Möchtest du die Kategorie "${model["currentCategory"].name}" wirklich löschen?</p>
                    </div>
                    <div class="modal-footer">
                        <a href="#" class="modal-action modal-close waves-effect waves-red btn-flat ">Abbrechen</a>
                        <a href="/categories/${model["currentCategory"].ID}/delete" class="modal-action modal-close waves-effect waves-green btn-flat ">Löschen</a>
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