<#import "/spring.ftl" as s>

<#macro settingsContainer formName containerId importScripts actionUrl>
    <form name="${formName}" method="post" action="<@s.url actionUrl/>">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" id="token"/>

        <#nested>
    </form>

    <div class="hidden securityContainerToastContent">
        <#if toastContent??>${toastContent}</#if>
    </div>

    <#if importScripts>
        <script src="<@s.url '/webjars/jquery/3.6.0/jquery.min.js'/>"></script>
        <script src="<@s.url '/webjars/materializecss/1.0.0/js/materialize.min.js'/>"></script>
        <script>
            initSettingsContainer('${formName}', '${containerId}');
        </script>
    </#if>
</#macro>
