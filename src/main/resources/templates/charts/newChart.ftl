<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "charts" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><#if chart.getID()??>${locale.getString("title.chart.edit")}<#else>${locale.getString("title.chart.new")}</#if></div>
                    </div>
                </div>
                <div class="container">
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="NewChart" action="<@s.url '/charts/newChart'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="<#if chart.getID()??>${chart.getID()?c}</#if>">

                        <#-- name -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="chart-name" type="text" name="name" <@validation.validation "name"/> value="<#if chart.getName()??>${chart.getName()}</#if>">
                                <label for="chart-name">${locale.getString("chart.new.label.name")}</label>
                            </div>
                        </div>

                        <#-- script -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <textarea id="chart-script" name="script" <@validation.validation "script" "materialize-textarea"/>><#if chart.getScript()??>${chart.getScript()}</#if></textarea>
                                <label for="chart-script">${locale.getString("chart.new.label.script")}</label>
                            </div>
                        </div>

                        <br>

                        <#-- info message if chart is not editable -->
                        <#if (chart.getType().name() == "DEFAULT")>
                            <div class="row no-margin-bottom">
                                <div class="col s12 center-align">
                                    <table class="text-color login-message no-border-table">
                                        <tr>
                                            <td><i class="material-icons">info</i></td>
                                            <td id="loginMessage">${locale.getString("chart.new.info.default")}</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </#if>

                        <#-- buttons -->
                        <div class="row hide-on-small-only">
                            <div class="col s6 right-align">
                                <a href="<@s.url '/charts/manage'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                            </div>

                            <div class="col s6 left-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action" <#if (chart.getType().name() == "DEFAULT")>disabled</#if>>
                                    <i class="material-icons left">save</i>${locale.getString("save")}
                                </button>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <a href="<@s.url '/charts/manage'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                                </div>
                            </div>
                            <div class="row center-align">
                                <div class="col s12">
                                    <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave" <#if (chart.getType().name() == "DEFAULT")>disabled</#if>>
                                        <i class="material-icons left">save</i>${locale.getString("save")}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>