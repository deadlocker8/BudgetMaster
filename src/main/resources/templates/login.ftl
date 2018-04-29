<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header "BudgetMaster"/>
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
                            <form action="/login" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="username" value="Default">

                                <div class="row">
                                    <div class="input-field col s12">
                                        <input id="password" type="password" name="password">
                                        <label for="password">${locale.getString("login.password")}</label>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col s12 right-align">
                                        <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                            <i class="material-icons left">send</i>${locale.getString("login.button")}
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>