<#import "/spring.ftl" as s>
<#import "error.ftl" as errorMacros>

<#assign code="418"/>
<@errorMacros.error code>
    <img id="teapot" src="<@s.url '/images/teapot.png'/>">
    <h1>418</h1>
    <h5>${locale.getString("errorpages.418")}</h5>
    <div>${locale.getString("errorpages.418.credits")}</div>
</@errorMacros.error>

