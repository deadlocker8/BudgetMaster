<#import "error.ftl" as errorMacros>

<#assign code="403"/>
<@errorMacros.error code>
    <h1>🔒 ${code}</h1>
    <h5>${locale.getString("errorpages." + code)}</h5>
</@errorMacros.error>