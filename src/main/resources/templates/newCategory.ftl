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
        <#assign locale = localization["tools.Localization"]>
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
                        <div class="grey-text text-darken-4 headline"><#if model["category"].getID()??>${locale.getString("title.category.edit")}<#else>${locale.getString("title.category.new")}</#if></div>
                    </div>
                </div>
                <div class="container">
                    <form name="NewCategory" action="/categories/newCategory" method="post">
                        <input type="hidden" name="ID" value="<#if model["category"].getID()??>${model["category"].getID()}</#if>">
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="category-name" type="text" name="name" value="<#if model["category"].getName()??>${model["category"].getName()}</#if>">
                                <label for="category-name">${locale.getString("category.new.label.name")}</label>
                            </div>
                        </div>
                        <input type="hidden" name="color" id="categoryColor" value="${model["category"].getColor()}">
                        <#list categoryColors as color>
                            <#if color?counter == 1 || color?counter == 7 || color?counter == 13>
                                <div class="row">
                                    <div class="col s2 m1 offset-m3 no-padding">
                                        <div class="category-color <#if color == model["category"].getColor()>category-color-active</#if>" style="background-color: ${color}"></div>
                                    </div>
                            <#else>
                                <div class="col s2 m1 no-padding">
                                    <div class="category-color <#if color == model["category"].getColor()>category-color-active</#if>" style="background-color: ${color}"></div>
                                </div>
                            </#if>

                            <#if color?counter == 6 || color?counter == 12>
                                </div>
                            </#if>
                        </#list>
                            <#--add custom color picker-->
                            <div class="col s2 m1 no-padding">
                                <div class="category-color <#if model["customColor"] == model["category"].getColor()>category-color-active</#if>" style="background-color: ${model["customColor"]}">
                                    +
                                </div>
                            </div>
                        </div>

                        <br>
                        <div class="row hide-on-small-only">
                            <div class="col m6 l4 offset-l2 right-align">
                                <a href="/categories" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                            </div>

                            <div class="col m6 l4 left-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">save</i>${locale.getString("save")}
                                </button>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <a href="/categories" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                                </div>
                            </div>
                            <div class="row center-align">
                                <div class="col s12">
                                    <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave">
                                        <i class="material-icons left">save</i>${locale.getString("save")}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!-- notificatiom modal -->
        <#if model["notifications"]??>
            <#if model["notifications"]?size == 1>
                <div id="notificationModal" class="modal">
                    <div class="modal-content">
                        <h5>${model["notifications"][0].messageTitle}</h5>
                        <#if model["notifications"][0].messageHeader??><h6>${model["notifications"][0].messageHeader}</h6></#if>
                        <p>${model["notifications"][0].messageBody}</p>
                    </div>
                    <div class="modal-footer">
                        <a class="modal-action modal-close waves-effect waves-green btn-flat">OK</a>
                    </div>
                </div>
            <#else>
                 <div id="notificationModal" class="modal">
                     <div class="modal-content">
                         <h5>Mehrere Meldungen</h5>
                         <#list model["notifications"] as notification>
                            <p>
                                ${notification.messageTitle}:
                                <#if notification.messageHeader??>${notification.messageHeader}</#if>
                                ${notification.messageBody}
                            </p>

                         </#list>
                     </div>
                     <div class="modal-footer">
                         <a class="modal-action modal-close waves-effect waves-green btn-flat">OK</a>
                     </div>
                 </div>
            </#if>
        </#if>

        <!--  Scripts-->
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
        <script src="/js/main.js"></script>
        <script src="/js/categories.js"></script>
    </body>
</html>