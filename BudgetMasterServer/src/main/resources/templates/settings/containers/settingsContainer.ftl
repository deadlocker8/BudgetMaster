<#import "/spring.ftl" as s>

<#macro settingsContainer formName containerId>
    <form name="${formName}" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" id="token"/>

        <#nested>
    </form>

    <div class="hidden securityContainerToastContent">
        <#if toastContent??>${toastContent}</#if>
    </div>

    <script src="<@s.url '/webjars/jquery/3.6.0/jquery.min.js'/>"></script>
    <script src="<@s.url '/webjars/materializecss/1.0.0/js/materialize.min.js'/>"></script>
    <script>
        // function initSettingsContainer() is not defined on first page load since the javascript file is loaded at the end
        if(typeof initSettingsContainer === "function")
        {
            initSettingsContainer('${formName}', '${containerId}');
        }
    </script>
</#macro>
