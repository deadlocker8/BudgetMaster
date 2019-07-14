<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "charts" settings/>

        <#import "chartFunctions.ftl" as chartFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("title.charts")}</div>
                    </div>
                </div>
                <div class="container">
                    <form name="ChartSettings" action="<@s.url '/charts/showChart'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="row no-margin-bottom">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("chart.steps.first")}
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2 no-margin-top">
                                <select name="chart">
                                    <#list charts as chart>
                                        <#assign chartName=chartFunctions.getChartName(chart)>
                                        <option value="${chart.getID()?c}">${chartName}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>

                        <div class="row no-margin-bottom">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("chart.steps.second")}
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2 no-margin-top">
                                <input id="chart-name" type="text" name="name" value="">
                                <label for="chart-name">${locale.getString("chart.new.label.name")}</label>
                            </div>
                        </div>

                        <div class="row no-margin-bottom">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("chart.steps.third")}
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2 no-margin-top">
                                <input id="chart-name" type="text" name="name" value="">
                                <label for="chart-name">${locale.getString("chart.new.label.name")}</label>
                            </div>
                        </div>

                        <br>

                        <#-- buttons -->
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave">
                                        <i class="material-icons left">show_chart</i>${locale.getString("chart.show")}
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