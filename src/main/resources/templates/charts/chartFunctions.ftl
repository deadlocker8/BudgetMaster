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