<html>
    <head>
        <#import "header.ftl" as header>
        <@header.header/>
        <#assign locale = localization["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar "about"/>

        <main>
            <div class="card main-card">
                <div class="container">
                    <div class="row">
                        <div class="col s8 offset-s2 center-align">
                            <img class="responsive-img" id="logo-huge" src="/images/Logo_with_text_medium_res.png">
                        </div>
                    </div>
                    <div class="hide-on-small-only"><br><br></div>
                    <div class="row">
                        <div class="col s4 m3 offset-m2 l2 offset-l3 bold">${locale.getString("about.version")}</div>
                        <div class="col s8 m5 l4">${locale.getString("version.name")} (${locale.getString("version.code")})</div>
                    </div>
                    <div class="row">
                        <div class="col s4 m3 offset-m2 l2 offset-l3 bold">${locale.getString("about.date")}</div>
                        <div class="col s8 m5 l4">${locale.getString("version.date")}</div>
                    </div>
                    <div class="row">
                        <div class="col s4 m3 offset-m2 l2 offset-l3 bold">${locale.getString("about.author")}</div>
                        <div class="col s8 m5 l4">${locale.getString("author")}</div>
                    </div>
                    <div class="row">
                        <div class="col s4 m3 offset-m2 l2 offset-l3 bold">${locale.getString("about.roadmap")}</div>
                        <div class="col s8 m5 l4"><a href="${locale.getString("roadmap.url")}">${locale.getString("about.roadmap.link")}</a></div>
                    </div>
                    <div class="row">
                        <div class="col s4 m3 offset-m2 l2 offset-l3 bold">${locale.getString("about.sourcecode")}</div>
                        <div class="col s8 m5 l4 break-all"><a href="${locale.getString("github.url")}">${locale.getString("github.url")}</a></div>
                    </div>
                    <div class="row">
                        <div class="col s4 m3 offset-m2 l2 offset-l3 bold">${locale.getString("about.credits")}</div>
                        <div class="col s8 m5 l4">${locale.getString("credits")}</div>
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