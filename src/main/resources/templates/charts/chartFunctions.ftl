<#import "/spring.ftl" as s>

<#function getChartName chart>
    <#if chart?? && chart.getName()??>
        <#if chart.getType().name() == "DEFAULT">
            <#return locale.getString(chart.getName())>
        <#else>
            <#return chart.getName()>
        </#if>
    <#else>
        <#return "">
    </#if>
</#function>

<#macro infoMessage text>
    <div class="row no-margin-bottom">
        <div class="col s12 center-align">
            <table class="text-color login-message no-border-table">
                <tr>
                    <td><i class="material-icons">info</i></td>
                    <td>${text}</td>
                </tr>
            </table>
        </div>
    </div>
</#macro>

<#macro buttonNew>
    <a href="<@s.url '/charts/newChart'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.chart.new")}</a>
</#macro>

<#macro buttonShow>
    <a href="<@s.url '/charts/manage'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">edit</i>${locale.getString("home.menu.charts.action.manage")}</a>
</#macro>

<#macro buttons>
    <div class="row hide-on-small-only valign-wrapper">
        <div class="col s6 right-align">
            <@buttonNew/>
        </div>
        <div class="col s6 left-align">
            <@buttonShow/>
        </div>
    </div>

    <div class="hide-on-med-and-up center-align">
        <div class="row center-align">
            <div class="row center-align">
                <div class="col s12">
                    <@buttonShow/>
                </div>
            </div>
            <div class="col s12">
                <@buttonNew/>
            </div>
        </div>
    </div>
</#macro>