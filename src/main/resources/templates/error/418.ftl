<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header "BudgetMaster - 418">
        <@header.style "login"/>
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
                                    <img id="teapot" src="/images/teapot.png">
                                    <h1>418</h1>
                                    <h5>${locale.getString("errorpages.418")}</h5>
                                    <div>${locale.getString("errorpages.418.credits")}</div>
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
        <#import "../scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>