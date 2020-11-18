<#import "/spring.ftl" as s>

<#macro homeEntry url icon iconColor headlineText bodyText>
    <div class="col s12 m6 l4 home-menu-cell">
        <a href="<@s.url url/>" class="home-menu-link btn-flat budget">
            <i class="material-icons icon-budget left ${iconColor}">${icon}</i>
            ${locale.getString(headlineText)}
        </a>
        <p class="text-grey home-menu-text">${locale.getString(bodyText)}</p>
        <div class="left-align">
            <#nested>

        </div>
    </div>
</#macro>

<#macro action url name>
    <a href="<@s.url url/>" class="waves-effect btn-flat home-menu-link-item"><i class="material-icons left">play_arrow</i>${locale.getString(name)}</a>
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

<#macro firstUseBanner>
    <div class="row">
        <div class="col s12 center-align">
            <div class="home-firstUseBanner-wrapper">
                <div class="home-firstUseBanner text-color">
                    <a href="<@s.url "/firstUse"/>" class="text-color">
                        <i class="fas fa-graduation-cap home-firstUseBanner-item"></i>
                        <span class="home-firstUseBanner-item">${locale.getString("home.first.use.teaser")}</span>
                    </a>
                    <a href="<@s.url "/settings/hideFirstUseBanner"/>" class="text-color home-firstUseBanner-item home-firstUseBanner-clear">
                        <i class="material-icons">clear</i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</#macro>