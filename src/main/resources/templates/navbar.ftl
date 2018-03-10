<#macro navbar activeID>
    <#assign locale = localization["tools.Localization"]>
    <ul id="slide-out" class="side-nav fixed">
        <@itemLogo/>
        <@itemDivider/>
        <@itemWithIcon "home", "/", locale.getString("menu.home"), "home", activeID/>
        <@itemWithIcon "payments", "", locale.getString("menu.payments"), "list", activeID/>
        <@subListStart "chart" locale.getString("menu.charts"), "show_chart" activeID/>
            <@itemPlain "chartCategories", "", locale.getString("menu.charts.chartCategories"), activeID/>
            <@itemPlain "chartMonth", "", locale.getString("menu.charts.chartMonth"), activeID/>
            <@itemPlain "chartTags", "", locale.getString("menu.charts.chartTags"), activeID/>
            <@itemPlain "chartCategoryBudget", "", locale.getString("menu.charts.chartCategoryBudget"), activeID/>
            <@itemPlain "chartHistogram", "", locale.getString("menu.charts.chartHistogram"), activeID/>
        <@subListEnd/>

        <@itemWithIcon "reports", "", locale.getString("menu.reports"), "description", activeID/>
        <@itemWithIcon "categories", "/categories", locale.getString("menu.categories"), "label", activeID/>
        <@itemWithIcon "settings", "", locale.getString("menu.settings"), "settings", activeID/>

        <@itemDivider/>
        <@itemWithIcon "about", "", locale.getString("menu.about"), "info", activeID/>

        <@itemDivider/>
        <@itemWithIcon "logout", "", locale.getString("menu.logout") "lock", activeID/>
    </ul>
    <a href="#" data-activates="slide-out" id="mobile-menu" class="mobile-menu"><i class="material-icons left mobile-menu-icon">menu</i>Menü</a>
    <div class="hide-on-large-only"><br></div>
</#macro>

<#macro itemLogo>
    <li><a href="/" class="waves-effect" id="nav-logo-container"><img id="nav-logo" src="/images/Logo_with_text.png"></a></li>
</#macro>

<#macro itemDivider>
    <li><div class="divider no-margin"></div></li>
</#macro>

<#macro itemPlain ID link text activeID>
    <li <#if activeID == ID>class="active"</#if>><a href="${link}" class="waves-effect"><span class="nav-margin">${text}</span></a></li>
</#macro>

<#macro itemWithIcon ID link text icon activeID>
    <li <#if activeID == ID>class="active"</#if>><a href="${link}" class="waves-effect"><i class="material-icons">${icon}</i>${text}</a></li>
</#macro>

<#macro subListStart ID text icon activeID>
<li>
    <ul class="collapsible collapsible-accordion no-padding">
        <li>
            <a class="collapsible-header nav-padding<#if activeID?starts_with(ID)> active</#if>"><i class="material-icons">${icon}</i>${text}</a>
            <div class="collapsible-body">
                <ul>
</#macro>

<#macro subListEnd >
                </ul>
            </div>
        </li>
    </ul>
</li>
</#macro>