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
                        <div class="headline">${locale.getString("menu.charts")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="<@s.url '/charts/newChart'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.chart.new")}</a></div>
                <br>
                <div class="container">
                    <table class="bordered">
                        <#list charts as chart>
                            <#assign chartName=chartFunctions.getChartName(chart)>
                            <thead>
                                <tr>
                                    <th>${locale.getString("chart.new.label.name")}</th>
                                    <th>${locale.getString("chart.type")}</th>
                                    <th>${locale.getString("chart.actions")}</th>
                                </tr>
                            </thead>
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

            <#if currentCategory??>
                <!-- confirm delete modal -->
                <div id="modalConfirmDelete" class="modal categoryDeleteModal background-color">
                    <div class="modal-content">
                        <h4>${locale.getString("info.title.category.delete")}</h4>
                        <p>${locale.getString("info.text.category.delete", currentCategory.name)}</p>
                        <p>${locale.getString("info.text.category.delete.move")}</p>

                        <form name="DestinationCategory" id="formDestinationCategory" action="<@s.url '/categories/${currentCategory.ID?c}/delete'/>" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <#import "../helpers/validation.ftl" as validation>
                            <@newTransactionMacros.categorySelect availableCategories preselectedCategory "col s12 m12 l8 offset-l2" locale.getString("info.title.category.delete.move")/>
                        </form>
                    </div>

                    <div class="modal-footer background-color">
                        <a href="<@s.url '/categories'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                        <a id="buttonDeleteCategory" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("delete")}</a>
                    </div>
                </div>
            </#if>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </body>
</html>