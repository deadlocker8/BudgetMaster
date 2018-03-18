<html>
    <head>
        <title>BudgetMaster - 500</title>
        <meta charset="UTF-8"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.css">
        <link type="text/css" rel="stylesheet" href="/css/style.css"/>
        <link type="text/css" rel="stylesheet" href="/css/login.css"/>

        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <#assign locale = static["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <main>
            <div class="row valign-wrapper full-height">
                <div class="col l4 offset-l4 m6 offset-m3 s10 offset-s1">
                    <div class="card">
                        <div class="card-content">
                            <span class="card-title">
                                <div id="logo-container" class="center-align"><img id="logo" src="/images/Logo_with_text_medium_res.png"></div>
                            </span>
                            <div class="row">
                                <div class="col s12 center-align">
                                    <h1>😔 500</h1>
                                    <h5>${locale.getString("errorpages.500")}</h5>
                                </div>
                            </div>
                            <div class="center-align">
                                <a href="/" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">home</i>${locale.getString("errorpages.home")}</a>
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