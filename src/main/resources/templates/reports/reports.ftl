<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
		<@header.header "BudgetMaster - ${locale.getString('menu.reports')}"/>
        <@header.style "reports"/>
        <@header.style "globalDatepicker"/>
        <@header.style "collapsible"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "reports" settings/>

        <#import "../transactions/transactionsMacros.ftl" as transactionsMacros>
        <#import "../filter/filterMacros.ftl" as filterMacros>

        <main>
            <div class="card main-card background-color">
                <#import "../helpers/globalDatePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate "/reports"/>

                <@header.content>
                    <br>

                    <div class="container">
                    <form name="NewReportSettings" action="<@s.url '/reports/generate'/>" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="${reportSettings.getID()?c}"/>
                        <input type="hidden" name="date" value="${dateService.getLongDateString(currentDate)}"/>

                        <div class="row">
                            <div class="col s12 center-align">
                                <@transactionsMacros.buttonFilter filterConfiguration.isActive()/>
                            </div>
                        </div>

                        <#-- settings -->
                        <div class="row">
                            <div class="col s12 center-align">
                                <div class="headline-small text-default">${locale.getString("report.settings")}</div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col s12 m8 offset-m2">
                                <div class="report-checkbox-container">
                                    <label>
                                        <input type="checkbox" name="includeBudget" <#if reportSettings.includeBudget>checked="checked"</#if>>
                                        <span class="columnName-checkbox-label text-default">${locale.getString('report.checkbox.include.budget')}</span>
                                    </label>
                                </div>
                                <div class="report-checkbox-container">
                                    <label>
                                        <input type="checkbox" name="splitTables" <#if reportSettings.splitTables>checked="checked"</#if>>
                                        <span class="columnName-checkbox-label text-default">${locale.getString('report.checkbox.split.tables')}</span>
                                    </label>
                                </div>
                                <div class="report-checkbox-container">
                                    <label>
                                        <input type="checkbox" name="includeCategoryBudgets" <#if reportSettings.includeCategoryBudgets>checked="checked"</#if>/>
                                        <span class="columnName-checkbox-label text-default">${locale.getString('report.checkbox.inclue.categorybudgets')}</span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <br>

                        <#-- columns -->
                        <div class="row no-margin">
                            <div class="col s12 center-align">
                                <div class="headline-small text-default">${locale.getString("report.columns")}</div>
                                <table class="no-border-table table-advice">
                                    <tr>
                                        <td><i class="material-icons">info_outline</i></td>
                                        <td>${locale.getString("report.columns.advice")}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col s12 m8 offset-m2">
                                <div id="columnNames">
                                    <#list reportSettings.getColumnsSorted() as column>
                                        <div class="columnName">
                                            <label>
                                                <input type="checkbox" class="columnName-checkbox" data-index="${column?index}" name="columns['${column?index}'].activated" <#if column.isActivated()>checked="checked"</#if>/>
                                                <span class="columnName-label">${locale.getString(column.getKey())}</span>
                                            </label>
                                            <input type="hidden" name="columns['${column?index}'].ID" value="${column.getID()}"/>
                                            <input type="hidden" name="columns['${column?index}'].key" value="${column.getKey()}"/>
                                            <input type="hidden" name="columns['${column?index}'].position" value=""/>
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>

                        <#-- button generate -->
                        <div class="row valign-wrapper">
                            <div class="col s12 center-align">
                                <@header.buttonSubmit name='buttonSave' icon='save' localizationKey='report.button.generate'/>
                            </div>
                        </div>
                    </form>
                </div>
                </@header.content>
            </div>

            <@filterMacros.filterModal filterConfiguration/>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/webjars/sortablejs/1.10.2/Sortable.min.js'/>"></script>
        <script src="<@s.url '/js/reports.js'/>"></script>
        <script src="<@s.url '/js/globalDatePicker.js'/>"></script>
        <script src="<@s.url '/js/filter.js'/>"></script>
        <script>document.cookie = "currentDate=${dateService.getDateStringNormal(currentDate)}";</script>
    </@header.body>
</html>