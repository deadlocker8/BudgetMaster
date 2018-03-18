<#macro validation fieldName classes="">
    <#assign locale = static["tools.Localization"]>
    <#if error?? && error.hasFieldErrors(fieldName)>
        class="tooltipped invalid ${classes}" data-position="bottom" data-delay="50" data-tooltip="${locale.getString(error.getFieldError(fieldName).getCode())}"
    <#else>
        class="validate ${classes}"
    </#if>
</#macro>