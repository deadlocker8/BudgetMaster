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
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "charts" settings/>

        <#import "chartFunctions.ftl" as chartFunctions>
        <#import "../transactions/transactionsMacros.ftl" as transactionsMacros>
        <#import "../filter/filterMacros.ftl" as filterMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">show_chart</i> ${locale.getString("title.charts")}</div>
                    </div>
                </div>

                <@header.content>
                    <br>

                    <div class="row">
                        <div class="col s12 center-align">
                            <@header.buttonLink url='' icon='edit' localizationKey='chart.button.settings' noUrl=true id='buttonShowChartSettings' classes='hidden'/>
                        </div>
                    </div>

                    <form name="NewChartSettings" action="<@s.url '/charts'/>" method="post" class="<#if chartSettings.isChartSelected()>hidden</#if>">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="container">
                            <div class="row">
                                <div class="col s12 center-align">
                                    <#list displayTypes as displayType>
                                        <@chartTypeButton item=displayType buttonClass="button-display-type" initialItem=chartSettings.getDisplayType()/>
                                    </#list>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" name="displayType" value="${chartSettings.getDisplayType().name()}">

                        <div class="container">
                            <div class="row">
                                <div class="col s12 center-align">
                                    <#list groupTypes as groupType>
                                        <@chartTypeButton item=groupType buttonClass="button-group-type" initialItem=chartSettings.getGroupType()/>
                                    </#list>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" name="groupType" value="${chartSettings.getGroupType().name()}">

                        <div class="container">
                            <div class="row">
                                <#list charts as chart>
                                    <@chartPreview chart/>
                                </#list>
                                <div class="col s12 center-align hidden" id="buttonCustomCharts">
                                    <@header.buttonLink url='/charts/manage' icon='edit' localizationKey='chart.button.manage'/>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" name="chartID" value="">

                        <@dateSelect/>

                        <@filterOptions/>

                        <#-- buttons -->
                        <div class="row center-align">
                            <div class="col s12">
                                <button class="btn waves-effect waves-light background-blue" type="submit" name="buttonSave" disabled>
                                    <i class="material-icons left">show_chart</i>${locale.getString("chart.show")}
                                </button>
                            </div>
                        </div>
                    </form>

                    <br>

                    <div class="container-chart">
                        <#if containerID??>
                            <div id="${containerID}" class="chart-canvas"></div>
                        </#if>
                    </div>
                </@header.content>
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
    </@header.body>
</html>


<#macro chartTypeButton item buttonClass initialItem>
    <#assign isInitialItem=item.name()==initialItem.name()/>

    <a class="waves-effect waves-light btn-large background-grey text-black ${buttonClass} <#if isInitialItem>active</#if>" data-value="${item.name()}">
        <#if item.hasFontAwesomeIcon()>
            <i class="${item.getIcon()} left"></i> ${locale.getString(item.getLocalizationKey())}
        <#else>
            <i class="material-icons left">${item.getIcon()}</i> ${locale.getString(item.getLocalizationKey())}
        </#if>
    </a>
</#macro>

<#macro chartPreview chart>
    <div class="col s6 m4 l3 center-align chart-preview-column hidden" data-display-type="${chart.getDisplayType()}" data-group-type="${chart.getGroupType()}" data-id="${chart.getID()?c}">
        <div class="card chart-preview background-grey-dark">
            <div class="card-image">
                <img src="<@s.url '/images/charts/' + chart.getPreviewImageFileName()!"placeholder.png"/>">
            </div>
            <div class="card-action bold valign-wrapper">
                <span style="margin: auto">
                    ${chartFunctions.getChartName(chart)}
                </span>
            </div>
        </div>
    </div>
</#macro>

<#macro dateSelect>
    <div class="container">
        <div class="row">
            <div class="col s12">
                <div class="card" id="chart-date-card">
                    <div class="card-content">
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

                        <@quickDateOptions/>

                        <script>
                            startDate = "${startDate}".split(".");
                            startDate = new Date(startDate[2], startDate[1] - 1, startDate[0]);
                            endDate = "${endDate}".split(".");
                            endDate = new Date(endDate[2], endDate[1] - 1, endDate[0]);
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro quickDateOptions>
    <div class="row no-margin-bottom">
        <div class="col s12 m12 l8 offset-l2 no-margin-top">
            <table class="no-border-table">
                <tr>
                    <@quickDateOption index="0" localizationKey="chart.quick.this.week"/>
                    <@quickDateOption index="1" localizationKey="chart.quick.this.month"/>
                    <@quickDateOption index="2" localizationKey="chart.quick.this.year"/>
                    <@quickDateOption index="3" localizationKey="chart.quick.all"/>
                </tr>
                <tr>
                    <@quickDateOption index="4" localizationKey="chart.quick.last.week"/>
                    <@quickDateOption index="5" localizationKey="chart.quick.last.month"/>
                    <@quickDateOption index="6" localizationKey="chart.quick.last.year"/>
                    <@quickDateOption index="7" localizationKey="chart.quick.until.endOfLastYear"/>
                </tr>
                <tr>
                    <@quickDateOption index="8" localizationKey="chart.quick.last.week.days"/>
                    <@quickDateOption index="9" localizationKey="chart.quick.last.month.days"/>
                    <@quickDateOption index="10" localizationKey="chart.quick.last.year.days"/>
                    <@quickDateOption index="11" localizationKey="chart.quick.until.today"/>
                </tr>
            </table>
        </div>
    </div>
</#macro>

<#macro quickDateOption index localizationKey>
    <td class="quick-date" data-quick="${index}">${locale.getString(localizationKey)}</td>
</#macro>

<#macro filterOptions>
    <div class="container">
        <div class="row">
            <div class="col s12 no-margin-top center-align">
                <ul class="collapsible">
                    <li>
                        <div class="collapsible-header"><i class="fas fa-filter"></i>${locale.getString("title.filter")} <span class="badge background-red hidden text-white" id="filterActiveBadge">${locale.getString("filter.active.short")}</span></div>
                        <div class="collapsible-body left-align">
                            <@filterMacros.filterModalContent chartSettings.getFilterConfiguration() "filterConfiguration"/>
                            <div class="center-align">
                                <@filterMacros.buttonResetChart/>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</#macro>
