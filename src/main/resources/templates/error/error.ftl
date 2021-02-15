<#macro error code>
    <html>
        <head>
            <#import "../helpers/header.ftl" as header>
            <@header.globals/>
            <@header.header "BudgetMaster - ${code}"/>
            <@header.style "login"/>
            <#import "/spring.ftl" as s>
        </head>
        <@header.body>
            <main>
                <div class="row valign-wrapper full-height">
                    <div class="col l4 offset-l4 m6 offset-m3 s10 offset-s1">
                        <div class="card background-color">
                            <div class="card-content">
                                <div class="card-title">
                                    <div id="logo-container" class="center-align"><@header.logo "logo" ""/></div>
                                </div>
                                <div class="row">
                                    <div class="col s12 center-align">
                                        <#nested>
                                    </div>
                                </div>
                                <div class="center-align">
                                    <a href="<@s.url '/'/>" class="waves-effect waves-light btn background-blue"><i class="material-icons left">home</i>${locale.getString("errorpages.home")}</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </@header.body>
    </html>
</#macro>