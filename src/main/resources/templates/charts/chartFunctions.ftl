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