<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "datepicker"/>
        <@header.style "filter"/>
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
                        <div class="headline">${locale.getString("title.charts")}</div>
                    </div>
                </div>
                <div class="container">
                    <form name="NewChartSettings" action="<@s.url '/charts/showChart'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <!-- STEP 1 -->
                        <div class="row no-margin-bottom">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("chart.steps.first")}
                            </div>
                        </div>
                        <div class="row">
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
                        </div>


                        <!-- STEP 2 -->
                        <div class="row no-margin-bottom">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("chart.steps.second")}
                            </div>
                        </div>
                        <div class="row">
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

                        <script>
                            startDate = "${startDate}".split(".");
                            startDate = new Date(startDate[2], startDate[1]-1, startDate[0]);
                            endDate = "${endDate}".split(".");
                            endDate = new Date(endDate[2], endDate[1]-1, endDate[0]);
                        </script>


                        <!-- STEP 3 -->
                        <div class="row no-margin-bottom">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("chart.steps.third")}
                            </div>
                        </div>
                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2 no-margin-top center-align">
                                <@transactionsMacros.buttonFilter chartSettings.getFilterConfiguration().isActive()/>
                            </div>
                        </div>

                        <@filterMacros.filterModalCharts chartSettings.getFilterConfiguration()/>

                        <br>

                        <#-- buttons -->
                        <div class="row center-align">
                            <div class="col s12">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave">
                                    <i class="material-icons left">show_chart</i>${locale.getString("chart.show")}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!-- Pass localization to JS -->
        <#import "../helpers/globalDatePicker.ftl" as datePicker>
        <@datePicker.datePickerLocalization/>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/charts.js'/>"></script>
        <script src="<@s.url '/js/filter.js'/>"></script>
    </body>
</html>