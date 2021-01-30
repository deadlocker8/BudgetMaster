<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.charts')}"/>
        <@header.style "datepicker"/>
        <@header.style "collapsible"/>
        <@header.style "charts"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "charts" settings/>

        <#import "chartFunctions.ftl" as chartFunctions>
        <#import "../transactions/transactionsMacros.ftl" as transactionsMacros>
        <#import "../filter/filterMacros.ftl" as filterMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">
                            <i class="material-icons">show_chart</i> ${locale.getString("title.charts")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align">
                    <a href="<@s.url '/charts/manage'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">edit</i>${locale.getString("home.menu.charts.action.manage")}
                    </a></div>
                <br>
                <div class="container">
                    <form name="NewChartSettings" action="<@s.url '/charts'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="row">
                            <div class="col s12">
                                <ul class="collapsible z-depth-2">
                                    <@stepOne/>

                                    <@stepTwo/>

                                    <@stepThree/>
                                </ul>
                            </div>
                        </div>

                        <@filterMacros.filterModalCharts chartSettings.getFilterConfiguration()/>

                        <#-- buttons -->
                        <div class="row center-align">
                            <div class="col s12">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit"
                                        name="buttonSave">
                                    <i class="material-icons left">show_chart</i>${locale.getString("chart.show")}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="container-chart">
                    <#if containerID??>
                        <div id="${containerID}" class="chart-canvas"></div>
                    </#if>
                </div>
            </div>
        </main>

        <!-- Pass localization to JS -->
        <#import "../helpers/globalDatePicker.ftl" as datePicker>
        <@datePicker.datePickerLocalization/>
        <script>
            filterActive = "${locale.getString("filter.active")}";
            filterNotActive = "${locale.getString("title.filter")}";
        </script>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/libs/plotly.min.js'/>"></script>
        <script src="<@s.url '/js/libs/moment.min.js'/>"></script>
        <script src="<@s.url '/js/charts.js'/>"></script>
        <script src="<@s.url '/js/filter.js'/>"></script>

        <#if chart??>
            <script>
                <#if chart.getType().name() == "DEFAULT">
                localizedLocale = '${locale.getString("locale")}';
                localizedTitle = '${locale.getString(chart.getName())}';
                localizedCurrency = '${settings.getCurrency()}';
                localizedData = JSON.parse('${locale.getString(chart.getName() + ".localization")}');
                </#if>

                <#assign chartScript = chart.getScript()/>
                <#assign chartScript = chartScript?replace("var transactionData = [];", "var transactionData = ${transactionData};")/>
                <#assign chartScript = chartScript?replace("containerID", "${containerID}")/>
                ${chartScript}
            </script>
        </#if>
    </body>
</html>


<#macro stepCollapsible step stepName isActive>
    <li <#if isActive>class="active"</#if>>
        <div class="collapsible-header">
            <span class="bold">${step}</span>
            <span class="step-name">${stepName}</span>
        </div>
        <div class="collapsible-body">
            <div class="row no-margin-bottom">
                <#nested>
            </div>
        </div>
    </li>
</#macro>

<#macro stepOne>
    <@stepCollapsible step=locale.getString("chart.steps.first.step") stepName=locale.getString("chart.steps.first") isActive=!chart??>
        <div class="input-field col s12 m12 l8 offset-l2 no-margin-top">
            <select name="chartID">
                <#list charts as chart>
                    <#assign chartName=chartFunctions.getChartName(chart)>
                    <#if chartSettings.getChartID() == chart.getID()>
                        <option selected value="${chart.getID()?c}">${chartName}</option>
                        <#continue>
                    </#if>

                    <option value="${chart.getID()?c}">${chartName}</option>
                </#list>
            </select>
        </div>
    </@stepCollapsible>
</#macro>

<#macro stepTwo>
    <@stepCollapsible step=locale.getString("chart.steps.second.step") stepName=locale.getString("chart.steps.second") isActive=false>
                    <div class="input-field col s6 m6 l4 offset-l2">
                        <#assign startDate = dateService.getLongDateString(chartSettings.getStartDate())/>

                        <input id="chart-datepicker" type="text" class="datepicker" name="startDate" value="${startDate}">
                        <label for="chart-datepicker">${locale.getString("chart.steps.second.label.start")}</label>
                    </div>

                    <div class="input-field col s6 m6 l4 ">
                        <#assign endDate = dateService.getLongDateString(chartSettings.getEndDate())/>

                        <input id="chart-datepicker-end" type="text" class="datepicker" name="endDate" value="${endDate}">
                        <label for="chart-datepicker-end">${locale.getString("chart.steps.second.label.end")}</label>
                    </div>
                </div>
                <div class="row no-margin-bottom">
                    <div class="col s12 m12 l8 offset-l2 no-margin-top">
                        <table class="no-border-table">
                            <tr>
                                <td class="quick-date" data-quick="0">${locale.getString("chart.quick.this.week")}</td>
                                <td class="quick-date" data-quick="1">${locale.getString("chart.quick.this.month")}</td>
                                <td class="quick-date" data-quick="2">${locale.getString("chart.quick.this.year")}</td>
                                <td class="quick-date" data-quick="3">${locale.getString("chart.quick.all")}</td>
                            </tr>
                            <tr>
                                <td class="quick-date" data-quick="4">${locale.getString("chart.quick.last.week.days")}</td>
                                <td class="quick-date" data-quick="5">${locale.getString("chart.quick.last.month.days")}</td>
                                <td class="quick-date" data-quick="6">${locale.getString("chart.quick.last.year.days")}</td>
                                <td class="quick-date" data-quick="7">${locale.getString("chart.quick.until.today")}</td>
                            </tr>
                        </table>
                    </div>
                    <div class="col s12 m12 l8 offset-l2 no-margin-top quick-date-container">
                    </div>

                <script>
                    startDate = "${startDate}".split(".");
                    startDate = new Date(startDate[2], startDate[1] - 1, startDate[0]);
                    endDate = "${endDate}".split(".");
                    endDate = new Date(endDate[2], endDate[1] - 1, endDate[0]);
                </script>
    </@stepCollapsible>
</#macro>

<#macro stepThree>
    <@stepCollapsible step=locale.getString("chart.steps.third.step") stepName=locale.getString("chart.steps.third") isActive=false>
        <div class="col s12 m12 l8 offset-l2 no-margin-top center-align">
            <@transactionsMacros.buttonFilter chartSettings.getFilterConfiguration().isActive()/>
        </div>
    </@stepCollapsible>
</#macro>