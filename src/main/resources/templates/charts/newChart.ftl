<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>

        <#if chart.getID()??>
            <#assign title=locale.getString("title.chart.edit")/>
        <#else>
            <#assign title=locale.getString("title.chart.new")/>
        </#if>

        <@header.header "BudgetMaster - ${title}"/>
        <#import "/spring.ftl" as s>
        <link rel="stylesheet" href="<@s.url "/webjars/codemirror/5.62.2/lib/codemirror.css"/>">
        <@header.style "charts"/>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "charts" settings/>

        <#import "chartFunctions.ftl" as chartFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${title}</div>
                    </div>
                </div>

                <@header.content>
                    <div class="container">
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="NewChart" action="<@s.url '/charts/newChart'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="<#if chart.getID()??>${chart.getID()?c}</#if>">

                        <#-- name -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <#assign chartName=""/>
                                <#if chart.getName()??>
                                    <#if chart.getType().name() == "DEFAULT">
                                        <#assign chartName=locale.getString(chart.getName())/>
                                    <#else>
                                        <#assign chartName=chart.getName()/>
                                    </#if>
                                </#if>
                                <i class="material-icons prefix">edit</i>
                                <input id="chart-name" type="text" name="name" <@validation.validation "name"/> value="${chartName}" <#if chart.getType().name() == "DEFAULT">disabled</#if>>
                                <label for="chart-name">${locale.getString("chart.new.label.name")}</label>
                            </div>
                        </div>

                        <!-- info message with link to wiki on how to create custom charts -->
                        <#if chart.getType().name() == "CUSTOM">
                            <@chartFunctions.infoMessage locale.getString("chart.new.info.wiki")/>
                        </#if>

                        <#-- script -->
                        <div class="row">
                            <div class="input-field col sl2" style="width: 100%">
                                <textarea id="chart-script" name="script" <@validation.validation "script" "materialize-textarea"/>>${chart.getScript()}</textarea>
                            </div>
                        </div>

                        <br>

                        <#-- info message if chart is not editable -->
                        <#if chart.getType().name() == "DEFAULT">
                            <@chartFunctions.infoMessage locale.getString("chart.new.info.default")/>
                        </#if>

                        <#-- buttons -->
                        <div class="row hide-on-small-only">
                            <div class="col s6 right-align">
                                <@header.buttonLink url='/charts/manage' icon='clear' localizationKey='cancel'/>
                            </div>

                            <div class="col s6 left-align">
                                <@header.buttonSubmit name='action' icon='save' localizationKey='save' id='button-save-account' disabled=(chart.getType().name() == "DEFAULT")/>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <@header.buttonLink url='/charts/manage' icon='clear' localizationKey='cancel'/>
                                </div>
                            </div>
                            <div class="row center-align">
                                <div class="col s12">
                                    <@header.buttonSubmit name='action' icon='save' localizationKey='save' id='button-save-account' disabled=(chart.getType().name() == "DEFAULT")/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                </@header.content>
            </div>
        </main>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/webjars/codemirror/5.62.2/lib/codemirror.js'/>"></script>
        <script src="<@s.url '/webjars/codemirror/5.62.2/mode/javascript/javascript.js'/>"></script>
        <script src="<@s.url '/js/charts.js'/>"></script>
    </@header.body>
</html>