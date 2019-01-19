<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "reports"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "reports"/>

        <main>
            <div class="card main-card background-color">
                <#import "../helpers/datePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate springMacroRequestContext.getRequestUri()/>

                <br>

                <div class="container">
                    <form name="NewReportSettings" action="<@s.url '/reports/generate'/>" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="date" value="${helpers.getLongDateString(currentDate)}"/>

                        <#-- settings -->
                        <div class="row">
                            <div class="col s12 center-align">
                                <div class="headline-small">${locale.getString("report.settings")}</div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col s12 m8 offset-m2">
                                <div class="report-checkbox-container">
                                    <label>
                                        <input type="checkbox" name="includeBudget" <#if reportSettings.includeBudget>checked="checked"</#if>>
                                        <span class="columnName-label">${locale.getString('report.checkbox.include.budget')}</span>
                                    </label>
                                </div>
                                <div class="report-checkbox-container">
                                    <label>
                                        <input type="checkbox" name="splitTables" <#if reportSettings.splitTables>checked="checked"</#if>>
                                        <span class="columnName-label">${locale.getString('report.checkbox.split.tables')}</span>
                                    </label>
                                </div>
                                <div class="report-checkbox-container">
                                    <label>
                                        <input type="checkbox" name="includeCategorybudgets" <#if reportSettings.includeCategoryBudgets>checked="checked"</#if>/>
                                        <span class="columnName-label">${locale.getString('report.checkbox.inclue.categorybudgets')}</span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <br>

                        <#-- columns -->
                        <div class="row no-margin">
                            <div class="col s12 center-align">
                                <div class="headline-small">${locale.getString("report.columns")}</div>
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
                                    <#list reportSettings.columns as key, value>
                                        <div class="columnName">
                                            <label>
                                                <input type="checkbox" class="columnName-checkbox" data-key="${key}" name="columns['${key}'].activated"/>
                                                <span class="columnName-label">${locale.getString(key)}</span>
                                            </label>
                                            <input type="hidden" name="columns['${key}'].position" value=""/>
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>

                        <#-- button new -->
                        <div class="row valign-wrapper">
                            <div class="col s12 center-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave">
                                    <i class="material-icons left">save</i>${locale.getString("report.button.generate")}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/sortable-1.8.1/Sortable.min.js'/>"></script>
        <script src="<@s.url '/js/reports.js'/>"></script>
        <script src="<@s.url '/js/datePicker.js'/>"></script>
        <script>document.cookie = "currentDate=${helpers.getDateString(currentDate)}";</script>
    </body>
</html>