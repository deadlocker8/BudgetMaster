<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
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
                        <div class="headline">${locale.getString("menu.charts")}</div>
                    </div>
                </div>
                <br>
                <@chartFunctions.buttons/>
                <br>
                <div class="container">
                    <table class="bordered">
                        <thead>
                            <tr>
                                <th>${locale.getString("chart.new.label.name")}</th>
                                <th>${locale.getString("chart.type")}</th>
                                <th>${locale.getString("chart.actions")}</th>
                            </tr>
                        </thead>
                        <#list charts as chart>
                            <#assign chartName=chartFunctions.getChartName(chart)>
                            <tr>
                                <td>${chartName}</td>
                                <td>
                                    <#if chart.getType().name() == "DEFAULT">
                                        <a class="no-padding text-color"><i class="material-icons left">lock</i></a>
                                    <#else>
                                        <a class="no-padding text-color"><i class="material-icons left">person</i></a>
                                    </#if>
                                </td>
                                <td>
                                    <a href="<@s.url '/charts/${chart.ID?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                    <#if (chart.getType().name() == "CUSTOM")>
                                        <a href="<@s.url '/charts/${chart.ID?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </table>
                    <#if charts?size == 0>
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    </#if>
                </div>
            </div>

            <#if currentChart??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal background-color">
                    <div class="modal-content">
                        <h4>${locale.getString("info.title.chart.delete")}</h4>
                        <p>${locale.getString("info.text.chart.delete", currentChart.getName())}</p>
                    </div>
                    <div class="modal-footer background-color">
                        <a href="<@s.url '/charts/manage'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                        <a href="<@s.url '/charts/${currentChart.getID()?c}/delete'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("info.title.chart.delete")}</a>
                    </div>
                </div>
            </#if>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/charts.js'/>"></script>
    </body>
</html>