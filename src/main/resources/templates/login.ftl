<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header/>
        <link type="text/css" rel="stylesheet" href="/css/login.css"/>

        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
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
                                <div class="input-field col s12">
                                    <input id="password" type="password" class="validate">
                                    <label for="password">Passwort</label>
                                </div>
                            </div>
                            <div class="right-align">
                                <a class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons right">send</i>Login</a>
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