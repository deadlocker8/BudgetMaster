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