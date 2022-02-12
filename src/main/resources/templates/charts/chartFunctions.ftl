<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

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
            <table class="text-default login-message no-border-table">
                <tr>
                    <td><i class="material-icons">info</i></td>
                    <td>${text}</td>
                </tr>
            </table>
        </div>
    </div>
</#macro>

<#macro buttonNew>
    <@header.buttonLink url='/charts/newChart' icon='add' localizationKey='title.chart.new'/>
</#macro>

<#macro buttonShow>
    <@header.buttonLink url='/charts' icon='show_chart' localizationKey='home.menu.charts.action.show'/>
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

<#macro iconForItem item>
    <#if item.hasFontAwesomeIcon()>
        <i class="${item.getIcon()} left"></i> ${locale.getString(item.getLocalizationKey())}
    <#else>
        <i class="material-icons left">${item.getIcon()}</i> ${locale.getString(item.getLocalizationKey())}
    </#if>
</#macro>
