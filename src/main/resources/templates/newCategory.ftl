<html>
    <head>
        <title>BudgetMaster</title>
        <meta charset="UTF-8"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.css">
        <link type="text/css" rel="stylesheet" href="/css/main.css"/>
        <link type="text/css" rel="stylesheet" href="/css/style.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    </head>
    <body class="budgetmaster-blue-light">
        <ul id="slide-out" class="side-nav fixed">
            <li><a href="" class="waves-effect" id="nav-logo-container"><img id="nav-logo" src="/images/Logo_with_text.png"></a></li>
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
            <li><div class="divider"></div></li>
            <li><a href="#!" class="waves-effect"><i class="material-icons">lock</i>Logout</a></li>
        </ul>
        <a href="#" data-activates="slide-out" id="mobile-menu" class="mobile-menu"><i class="material-icons left mobile-menu-icon">menu</i>Menü</a>
        <div class="hide-on-large-only"><br></div>

        <main>
            <div class="card main-card">
                <div class="container">
                    <div class="section center-align">
                        <div class="grey-text text-darken-4 headline">Neue Kategorie</div>
                    </div>
                </div>
                <div class="container">
                    <form>
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="category-name" type="text" class="validate">
                                <label for="category-name">Name</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="category-color" type="text" class="validate">
                                <label for="category-color">Farbe</label>
                            </div>
                        </div>
                        <div class="row center-align hide-on-small-only">
                            <div class="col m12 l8 offset-l2">
                                <a href="/categories" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>Abbrechen</a>
                                <span class="button-spacer"></span>
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">save</i>Speichern
                                </button>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <a href="/categories" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>Abbrechen</a>
                                </div>
                            </div>
                            <div class="row center-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">save</i>Speichern
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
        <script src="/js/main.js"></script>
    </body>
</html>