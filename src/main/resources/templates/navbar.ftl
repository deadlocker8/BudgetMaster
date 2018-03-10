<#macro navbar activeID>
    <ul id="slide-out" class="side-nav fixed">
        <!-- TODO: deselect everything when selecting a sublist-->

        <@itemLogo/>
        <@itemDivider/>
        <@itemWithIcon "home", "/", "Startseite", "home", activeID/>
        <@itemWithIcon "payments", "", "Buchungen", "list", activeID/>
        <@subListStart "Diagramme", "show_chart"/>
            <@itemPlain "chartCategories", "", "Eingaben/Ausgaben nach Kategorien", activeID/>
            <@itemPlain "chartMonth", "", "Eingaben/Ausgaben pro Monat", activeID/>
            <@itemPlain "chartTags", "", "Eingaben/Ausgaben nach Tags", activeID/>
            <@itemPlain "chartCategoryBudget", "", "Verbrauch nach Kategorien", activeID/>
            <@itemPlain "chartHistogram", "", "Histogramm", activeID/>
        <@subListEnd/>

        <@itemWithIcon "reports", "", "Berichte", "description", activeID/>
        <@itemWithIcon "categories", "/categories", "Kategorien", "label", activeID/>
        <@itemWithIcon "settings", "", "Einstellungen", "settings", activeID/>

        <@itemDivider/>
        <@itemWithIcon "about", "", "Über", "info", activeID/>

        <@itemDivider/>
        <@itemWithIcon "logout", "", "Logout", "lock", activeID/>
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

<#macro subListStart text icon>
<li>
    <ul class="collapsible collapsible-accordion no-padding">
        <li>
            <a class="collapsible-header nav-padding"><i class="material-icons">${icon}</i>${text}</a>
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