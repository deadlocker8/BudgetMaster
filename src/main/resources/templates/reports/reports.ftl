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
                                    <input type="checkbox" id="report-checkbox-include-budget"/>
                                    <span class="columnName-label">${locale.getString('report.checkbox.include.budget')}</span>
                                </label>
                            </div>
                            <div class="report-checkbox-container">
                                <label>
                                    <input type="checkbox" id="report-checkbox-split-tables">
                                    <span class="columnName-label">${locale.getString('report.checkbox.split.tables')}</span>
                                </label>
                            </div>
                            <div class="report-checkbox-container">
                                <label>
                                    <input type="checkbox" id="report-checkbox-include-categorybudgets"/>
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
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label">${locale.getString('report.position')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label">${locale.getString('report.date')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label">${locale.getString('report.repeating')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label">${locale.getString('report.category')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label">${locale.getString('report.name')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label">${locale.getString('report.description')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label text-color">${locale.getString('report.tags')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label text-color">${locale.getString('report.account')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label text-color">${locale.getString('report.rating')}</span>
                                    </label>
                                </div>
                                <div class="columnName">
                                    <label>
                                        <input type="checkbox" class="columnName-checkbox"/>
                                        <span class="columnName-label text-color">${locale.getString('report.amount')}</span>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                <#-- button new -->
                    <div class="row valign-wrapper">
                        <div class="col s12 center-align"><a href="<@s.url '/reports/generate'/>"
                                                             class="waves-effect waves-light btn budgetmaster-blue"><i
                                class="material-icons left">save</i>${locale.getString("report.button.generate")}</a></div>
                    </div>
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