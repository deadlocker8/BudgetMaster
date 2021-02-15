<#import "/spring.ftl" as s>

<#macro navbar activeID settings>
    <ul id="slide-out" class="sidenav sidenav-fixed">
        <@itemLogo/>
        <@itemDivider/>
        <@itemSearch/>
        <@itemDivider/>
        <@itemAccountSelect/>
        <@itemDivider/>
        <@itemWithIcon "home", "/", locale.getString("menu.home"), "home", "background-blue", activeID/>
        <@itemWithIcon "transactions", "/transactions", locale.getString("menu.transactions"), "list", "background-blue-baby", activeID/>
        <@itemWithIcon "templates", "/templates", locale.getString("menu.templates"), "file_copy", "background-orange-dark", activeID/>
        <@itemWithIcon "charts" "/charts" locale.getString("menu.charts"), "show_chart" "background-purple", activeID/>
        <@itemWithIcon "reports", "/reports", locale.getString("menu.reports"), "description", "background-green", activeID/>
        <@itemWithIcon "categories", "/categories", locale.getString("menu.categories"), "label", "background-orange", activeID/>
        <@itemWithIcon "settings", "/settings", locale.getString("menu.settings"), "settings", "background-red", activeID/>

        <@itemDivider/>
        <@itemWithIcon "hotkeys", "/hotkeys", locale.getString("menu.hotkeys"), "keyboard", "background-grey", activeID/>
        <@itemWithFontawesomeIcon "firstUseGuide", "/firstUse", locale.getString("menu.firstUseGuide"), "fas fa-graduation-cap", "background-grey", activeID/>
        <@itemWithIcon "about", "/about", locale.getString("menu.about"), "info", "background-grey", activeID/>

        <@itemDivider/>
        <@itemLogout locale.getString("menu.logout") "lock"/>

        <#if updateCheckService.isUpdateAvailable()>
            <@itemDivider/>
            <@itemUpdate "/settings/update", locale.getString("menu.update"), "system_update"/>
        </#if>

        <#if programArgs.isTest()>
            <@itemDivider/>
            <@itemDebug "TEST MODE" "report_problem"/>
        </#if>

        <#if programArgs.isDebug()>
            <@itemDivider/>
            <@itemDebug "DEBUG MODE" "bug_report"/>
        </#if>
    </ul>
    <a href="#" data-target="slide-out" class="sidenav-trigger white-text valign-wrapper"><i class="material-icons left mobile-menu-icon">menu</i>Menü</a>
    <div class="hide-on-large-only"><br></div>

    <#--logout form -->
    <form class="hide" id="logout-form" action="<@s.url '/logout'/>" method="post">
        <#if _csrf??>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="mess" value=<%=n%>
        </#if>
    </form>

    <@backupReminder settings/>
    <@whatsNewModal settings/>
</#macro>

<#macro itemLogo>
    <li><a href="<@s.url '/'/>" class="waves-effect" id="nav-logo-container"><@header.logo "nav-logo" ""/></a></li>
</#macro>

<#macro itemDivider>
    <li><div class="divider no-margin"></div></li>
</#macro>

<#macro itemSearch>
    <nav class="searchWrapper">
        <div class="nav-wrapper">
            <form id="navbarSearchForm" action="<@s.url '/search'/>" method="get">
                <input type="hidden" name="searchName" value="true">
                <input type="hidden" name="searchDescription" value="true">
                <input type="hidden" name="searchCategory" value="true">
                <input type="hidden" name="searchTags" value="true">

                <div class="input-field">
                    <input id="search" class="text-default mousetrap" name="searchText" type="search">
                    <label class="label-icon" for="search"><i class="material-icons">search</i></label>
                    <i id="buttonSearch" class="material-icons">send</i>
                    <i id="buttonClearSearch" class="material-icons">close</i>
                </div>
            </form>
        </div>
    </nav>
</#macro>

<#macro itemAccountSelect>
<div class="account-navbar center-align">
    <div class="input-field no-margin" id="selectWrapper">
        <select id="selectAccount">
            <#list helpers.getAllAccounts() as account>
                <#if (account.getType().name() == "ALL")>
                    <option <#if account.isSelected()>selected</#if> value="${account.getID()?c}">${locale.getString("account.all")}</option>
                <#else>
                    <option <#if account.isSelected()>selected</#if> value="${account.getID()?c}">${account.getName()}</option>
                </#if>
            </#list>
        </select>
    </div>

    <a href="<@s.url '/accounts'/>">${locale.getString("home.menu.accounts.action.manage")}</a>

    <#assign accountBudget = helpers.getAccountBudget()/>
    <#if accountBudget <= 0>
        <div class="account-budget ${redTextColor}">${currencyService.getCurrencyString(accountBudget)}</div>
    <#else>
        <div class="account-budget ${greenTextColor}">${currencyService.getCurrencyString(accountBudget)}</div>
    </#if>
    <div class="account-budget-date text-default">(${locale.getString("account.budget.asof")}: ${dateService.getDateStringNormal(dateService.getCurrentDate())})</div>
</div>
</#macro>

<#macro itemPlain ID link text activeID>
    <li <#if activeID == ID>class="active"</#if>><a href="<@s.url '${link}'/>" class="waves-effect"><span class="nav-margin">${text}</span></a></li>
</#macro>

<#macro itemWithIcon ID link text icon activeColor activeID>
    <#if activeID == ID>
        <li class="active"><a href="<@s.url '${link}'/>" class="waves-effect no-padding"><div class="stripe ${activeColor}"></div><i class="material-icons">${icon}</i>${text}</a></li>
    <#else>
        <li><a href="<@s.url '${link}'/>" class="waves-effect"><i class="material-icons">${icon}</i>${text}</a></li>
    </#if>
</#macro>

<#macro itemWithFontawesomeIcon ID link text icon activeColor activeID>
    <#if activeID == ID>
        <li class="active"><a href="<@s.url '${link}'/>" class="waves-effect no-padding"><div class="stripe ${activeColor}"></div><i class="${icon}"></i>${text}</a></li>
    <#else>
        <li><a href="<@s.url '${link}'/>" class="waves-effect"><i class="${icon}"></i>${text}</a></li>
    </#if>
</#macro>

<#macro itemLogout text icon>
    <li><a class="waves-effect" id="button-logout"><i class="material-icons">${icon}</i>${text}</a></li>
</#macro>

<#macro itemUpdate link text icon>
    <li><a href="<@s.url '${link}'/>" class="waves-effect background-yellow budgetmaster-text-update"><i class="material-icons" id="icon-update">${icon}</i>${text}</a></li>
</#macro>

<#macro itemDebug text icon>
    <li><a class="waves-effect background-red budgetmaster-text-update"><i class="material-icons" id="icon-update">${icon}</i>${text}</a></li>
</#macro>

<#macro backupReminder settings>
    <#if settings.needToShowBackupReminder()>
        <div id="modalBackupReminder" class="modal background-color">
            <div class="modal-content">
                <h4>${locale.getString("info.title.backup.reminder")}</h4>
                <p>${locale.getString("info.text.backup.reminder")}</p>
            </div>
            <div class="modal-footer background-color">
                <a href="<@s.url '/backupReminder/cancel'/>" id="buttonCloseReminder" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                <a href="<@s.url '/backupReminder/settings'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("info.button.backup.reminder")}</a>
            </div>
        </div>
    </#if>
</#macro>

<#macro whatsNewModal settings>
    <#if settings.needToShowWhatsNew()>
        <div id="whatsNewModelContainer" data-url="<@s.url '/about/whatsNewModal'/>"></div>
    </#if>
</#macro>