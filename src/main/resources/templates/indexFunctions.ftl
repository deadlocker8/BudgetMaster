<#import "/spring.ftl" as s>
<#import "helpers/header.ftl" as header>

<#macro homeEntry url icon iconColor headlineText bodyText>
    <div class="col s12 m6 l4 home-menu-cell">
        <@header.buttonFlat url=url icon=icon localizationKey=headlineText classes="home-menu-link budget" iconClasses='icon-budget ' + iconColor/>
        <p class="text-grey home-menu-text">${locale.getString(bodyText)}</p>
        <div class="left-align">
            <#nested>

        </div>
    </div>
</#macro>

<#macro action url name>
    <@header.buttonFlat url=url icon='play_arrow' localizationKey=name classes="home-menu-link-item"/>
</#macro>

<#macro stepContent headline contentText actionUrl, actionName>
    <h5>${locale.getString(headline)}</h5>
    <p>
        ${locale.getString(contentText)}
        <#nested>
    </p>
    <p>
        <#if actionUrl?has_content>
            <@indexFunctions.action url=actionUrl name=actionName/>
        </#if>
    </p>
</#macro>
