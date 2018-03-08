<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header/>
    </head>
    <body class="budgetmaster-blue-light">
        <ul id="slide-out" class="side-nav fixed">
            <li><a href="/" class="waves-effect" id="nav-logo-container"><img id="nav-logo" src="/images/Logo_with_text.png"></a></li>
            <li><div class="divider"></div></li>
            <li class="active"><a href="/" class="waves-effect"><i class="material-icons">home</i>Startseite</a></li>
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
            <li><a href="/categories" class="waves-effect"><i class="material-icons">label</i>Kategorien</a></li>
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
                        <a href="" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-chevron">chevron_left</i></a>
                        <a href="" class="waves-effect grey-text text-darken-4 headline-date">September 2018</a>
                        <a href="" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-chevron">chevron_right</i></a>
                    </div>
                </div>
                <div class="hide-on-small-only"><br></div>
                <div class="container">
                    <div class="row">
                        <div class="col s12 m4">
                            <div class="icon-block">
                                <h1 class="center text-green"><i class="material-icons icon-budget">file_download</i></h1>
                                <h5 class="center budget">2350,15 €</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">Einnahmen</h5>
                            </div>
                        </div>
                        <div class="col s12 m4">
                            <div class="icon-block">
                                <h1 class="center text-red"><i class="material-icons icon-budget">file_upload</i></h1>
                                <h5 class="center budget">-576,33 €</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">Ausgaben</h5>
                            </div>
                        </div>
                        <div class="col s12 m4">
                            <div class="icon-block">
                                <h1 class="center budgetmaster-blue-text"><i class="material-icons icon-budget">account_balance</i></h1>
                                <h5 class="center budget">1773,82 €</h5>
                                <h5 class="center grey-text text-darken-1 budget-headline">Rest</h5>
                            </div>
                        </div>
                    </div>
                    <div class="hide-on-small-only"><br><br></div>
                    <div class="row">
                        <div class="col s12">
                            <div class="budget-bar-container">
                                <div class="budget-bar color-green" style="width: 100%"></div>
                            </div>
                            <div class="budget-bar-container">
                                <div class="budget-bar color-red" style="width: 25%"></div>
                            </div>
                            <div class="budget-bar-container">
                                <div class="budget-bar budgetmaster-blue" style="width: 75%"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
        <script src="/js/main.js"></script>
    </body>
</html>