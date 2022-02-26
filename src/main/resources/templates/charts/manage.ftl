<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.charts')}"/>
        <@header.style "charts"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
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

                <@header.content>
                    <br>
                    <@chartFunctions.buttons/>
                    <br>
                    <div class="container">
                        <div class="section center-align">
                            <div class="headline-small">${locale.getString("charts.custom")}</div>
                        </div>

                        <table class="bordered">
                            <thead>
                                <tr>
                                    <th>${locale.getString("chart.new.label.name")}</th>
                                    <th>${locale.getString("chart.actions")}</th>
                                </tr>
                            </thead>
                            <#list customCharts as chart>
                                <#assign chartName=chartFunctions.getChartName(chart)>
                                <tr>
                                    <td>${chartName}</td>
                                    <td>
                                        <@header.buttonFlat url='/charts/' + chart.ID?c + '/edit' icon='edit' localizationKey='' classes="no-padding text-default"/>
                                        <@header.buttonFlat url='/charts/' + chart.ID?c + '/requestDelete' icon='delete' localizationKey='' classes="no-padding text-default button-request-delete-chart" isDataUrl=true/>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                        <#if customCharts?size == 0>
                            <div class="center-align charts-default-margin">${locale.getString("placeholder")}</div>
                            <br>
                        </#if>

                        <div class="section center-align charts-default-margin">
                            <div class="headline-small">${locale.getString("charts.default")}</div>
                        </div>

                        <table class="bordered">
                            <thead>
                                <tr>
                                    <th>${locale.getString("chart.new.label.name")}</th>
                                    <th>${locale.getString("chart.display.type")}</th>
                                    <th>${locale.getString("chart.group.type")}</th>
                                    <th>${locale.getString("chart.actions")}</th>
                                </tr>
                            </thead>
                            <#list defaultCharts as chart>
                                <#assign chartName=chartFunctions.getChartName(chart)>
                                <tr>
                                    <td>${chartName}</td>
                                    <td><@chartFunctions.iconForItem item=chart.getDisplayType()/></td>
                                    <td><@chartFunctions.iconForItem item=chart.getGroupType()/></td>
                                    <td>
                                        <@header.buttonFlat url='/charts/' + chart.ID?c + '/edit' icon='visibility' localizationKey='' classes="no-padding text-default"/>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </@header.content>
            </div>

            <div id="deleteModalContainerOnDemand"></div>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/charts.js'/>"></script>
    </@header.body>
</html>