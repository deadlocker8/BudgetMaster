<#macro navbar activeID>
    <#assign locale = static["tools.Localization"]>
    <ul id="slide-out" class="side-nav fixed">
        <@itemLogo/>
        <@itemDivider/>
        <@itemWithIcon "home", "/", locale.getString("menu.home"), "home", "budgetmaster-blue", activeID/>
        <@itemWithIcon "payments", "/payments", locale.getString("menu.payments"), "list", "budgetmaster-baby-blue", activeID/>
        <@subListStart "chart" locale.getString("menu.charts"), "show_chart" "budgetmaster-purple", activeID/>
            <@itemPlain "chartCategories", "", locale.getString("menu.charts.chartCategories"), activeID/>
            <@itemPlain "chartMonth", "", locale.getString("menu.charts.chartMonth"), activeID/>
            <@itemPlain "chartTags", "", locale.getString("menu.charts.chartTags"), activeID/>
            <@itemPlain "chartCategoryBudget", "", locale.getString("menu.charts.chartCategoryBudget"), activeID/>
            <@itemPlain "chartHistogram", "", locale.getString("menu.charts.chartHistogram"), activeID/>
        <@subListEnd/>

        <@itemWithIcon "reports", "/reports", locale.getString("menu.reports"), "description", "budgetmaster-green", activeID/>
        <@itemWithIcon "categories", "/categories", locale.getString("menu.categories"), "label", "budgetmaster-orange", activeID/>
        <@itemWithIcon "settings", "/settings", locale.getString("menu.settings"), "settings", "budgetmaster-red", activeID/>

        <@itemDivider/>
        <@itemWithIcon "about", "/about", locale.getString("menu.about"), "info", "budgetmaster-grey", activeID/>

        <@itemDivider/>
        <@itemWithIcon "logout", "/logout", locale.getString("menu.logout") "lock", "budgetmaster-red", activeID/>
    </ul>
    <a href="#" data-activates="slide-out" id="mobile-menu" class="mobile-menu"><i class="material-icons left mobile-menu-icon">menu</i>Menü</a>
    <div class="hide-on-large-only"><br></div>
</#macro>

<#macro itemLogo>
    <li><a href="/" class="waves-effect" id="nav-logo-container"><img id="nav-logo" src="/images/Logo_with_text_medium_res.png"></a></li>
</#macro>

<#macro itemDivider>
    <li><div class="divider no-margin"></div></li>
</#macro>

<#macro itemPlain ID link text activeID>
    <li <#if activeID == ID>class="active"</#if>><a href="${link}" class="waves-effect"><span class="nav-margin">${text}</span></a></li>
</#macro>

<#macro itemWithIcon ID link text icon activeColor activeID>
    <#if activeID == ID>
        <li class="active"><a href="${link}" class="waves-effect no-padding"><div class="stripe ${activeColor}"></div><i class="material-icons">${icon}</i>${text}</a></li>
    <#else>
        <li><a href="${link}" class="waves-effect"><i class="material-icons">${icon}</i>${text}</a></li>
    </#if>
</#macro>

<#macro subListStart ID text icon activeColor activeID>
<li>
    <ul class="collapsible collapsible-accordion no-padding">
        <li>
            <#if activeID?starts_with(ID)>
                <a href="/charts" class="collapsible-header no-padding active"><div class="stripe ${activeColor}"></div><i class="material-icons">${icon}</i>${text}</a>
            <#else>
                <a href="/charts" class="collapsible-header nav-padding"><i class="material-icons">${icon}</i>${text}</a>
            </#if>
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