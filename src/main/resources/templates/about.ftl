<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.about')}"/>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "about" settings/>
        <#import "/spring.ftl" as s>

        <main>
            <div class="card main-card background-color">
                <div class="row">
                    <div class="col s8 offset-s2 center-align">
                        <@header.logo "logo-huge" "responsive-img"/>
                    </div>
                </div>
                <div class="hide-on-small-only"><br><br></div>
                <div class="row">
                    <@cellKey locale.getString("about.version")/>
                    <div class="col s8 m5 l5">
                        ${build.getVersionName()} (${build.getVersionCode()})

                        <a class="whatsNewLink" data-url="<@s.url '/about/whatsNewModal'/>">${locale.getString("about.version.whatsnew")}?</a>
                        <div id="whatsNewModelContainerOnDemand"></div>
                    </div>
                </div>
                <div class="row">
                    <@cellKey locale.getString("about.date")/>
                    <div class="col s8 m5 l5">${build.getVersionDate()}</div>
                </div>
                <div class="row">
                    <@cellKey locale.getString("about.author")/>
                    <div class="col s8 m5 l5">${build.getAuthor()}</div>
                </div>
                <div class="row">
                    <@cellKey locale.getString("about.roadmap")/>
                    <div class="col s8 m5 l5"><a target="_blank" href="${locale.getString("roadmap.url")}">${locale.getString("about.roadmap.link")}</a></div>
                </div>
                <div class="row">
                    <@cellKey locale.getString("about.sourcecode")/>
                    <div class="col s8 m5 l5 break-all"><a target="_blank" href="${locale.getString("github.url")}">${locale.getString("github.url")}</a></div>
                </div>
                <div class="row">
                    <@cellKey locale.getString("about.credits")/>
                    <div class="col s8 m5 l5">${locale.getString("credits")}</div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/about.js'/>"></script>
    </body>
</html>

<#macro cellKey key>
    <div class="col s4 m3 offset-m2 l2 offset-l3 bold">${key}</div>
</#macro>