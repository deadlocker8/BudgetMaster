<#macro navbar activeID>
    <ul id="slide-out" class="sidenav sidenav-fixed">
        <@itemLogo/>
        <@itemDivider/>
        <@itemAccountSelect/>
        <@itemDivider/>
        <@itemWithIcon "home", "/", locale.getString("menu.home"), "home", "budgetmaster-blue", activeID/>
        <@itemWithIcon "accounts", "/accounts", locale.getString("menu.accounts"), "account_balance", "budgetmaster-grey", activeID/>
        <@itemWithIcon "transactions", "/transactions", locale.getString("menu.transactions"), "list", "budgetmaster-baby-blue", activeID/>
        <@subListStart "chart" locale.getString("menu.charts"), "show_chart" "budgetmaster-purple", activeID/>
            <#-- disabled until future versions -->
            <#--<@itemPlain "chartCategories", "", locale.getString("menu.charts.chartCategories"), activeID/>-->
            <#--<@itemPlain "chartMonth", "", locale.getString("menu.charts.chartMonth"), activeID/>-->
            <#--<@itemPlain "chartTags", "", locale.getString("menu.charts.chartTags"), activeID/>-->
            <#--<@itemPlain "chartCategoryBudget", "", locale.getString("menu.charts.chartCategoryBudget"), activeID/>-->
            <#--<@itemPlain "chartHistogram", "", locale.getString("menu.charts.chartHistogram"), activeID/>-->
        <@subListEnd/>

        <@itemWithIcon "reports", "/reports", locale.getString("menu.reports"), "description", "budgetmaster-green", activeID/>
        <@itemWithIcon "categories", "/categories", locale.getString("menu.categories"), "label", "budgetmaster-orange", activeID/>
        <@itemWithIcon "settings", "/settings", locale.getString("menu.settings"), "settings", "budgetmaster-red", activeID/>

        <@itemDivider/>
        <@itemWithIcon "about", "/about", locale.getString("menu.about"), "info", "budgetmaster-grey", activeID/>

        <@itemDivider/>
        <@itemWithIconNoRootUrl "logout", "javascript:\" onclick=\"$('#logout-form').submit();\"", locale.getString("menu.logout") "lock", "budgetmaster-red", activeID/>

        <#if helpers.isUpdateAvailable()>
            <@itemDivider/>
            <@itemUpdate "/update", locale.getString("menu.update"), "system_update"/>
        </#if>

        <#if programArgs.isDebug()>
            <@itemDivider/>
            <@itemDebug "DEBUG MODE" "bug_report"/>
        </#if>
    </ul>
    <a href="#" data-target="slide-out" class="sidenav-trigger white-text"><i class="material-icons left mobile-menu-icon">menu</i>Menü</a>
    <div class="hide-on-large-only"><br></div>

    <#--logout form -->
    <form class="hide" id="logout-form" action="<@s.url '/logout'/>" method="post">
        <#if _csrf??>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="mess" value=<%=n%>
        </#if>
    </form>

</#macro>

<#macro itemLogo>
    <#import "header.ftl" as header>
    <#import "/spring.ftl" as s>
    <li><a href="<@s.url '/'/>" class="waves-effect" id="nav-logo-container"><@header.logo "nav-logo" ""/></a></li>
</#macro>

<#macro itemDivider>
    <li><div class="divider no-margin"></div></li>
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
    <#assign accountBudget = helpers.getAccountBudget()/>
    <#if accountBudget <= 0>
        <div class="account-budget ${redTextColor}">${helpers.getCurrencyString(accountBudget)}</div>
    <#else>
        <div class="account-budget ${greenTextColor}">${helpers.getCurrencyString(accountBudget)}</div>
    </#if>
    <div class="account-budget-date text-color">(${locale.getString("account.budget.asof")}: ${helpers.getDateString(helpers.getCurrentDate())})</div>
</div>
</#macro>

<#macro itemPlain ID link text activeID>
    <#import "/spring.ftl" as s>
    <li <#if activeID == ID>class="active"</#if>><a href="<@s.url '${link}'/>" class="waves-effect"><span class="nav-margin">${text}</span></a></li>
</#macro>

<#macro itemWithIcon ID link text icon activeColor activeID>
    <#import "/spring.ftl" as s>
    <#if activeID == ID>
        <li class="active"><a href="<@s.url '${link}'/>" class="waves-effect no-padding"><div class="stripe ${activeColor}"></div><i class="material-icons">${icon}</i>${text}</a></li>
    <#else>
        <li><a href="<@s.url '${link}'/>" class="waves-effect"><i class="material-icons">${icon}</i>${text}</a></li>
    </#if>
</#macro>

<#macro itemWithIconNoRootUrl ID link text icon activeColor activeID>
    <#if activeID == ID>
        <li class="active"><a href="${link}" class="waves-effect no-padding"><div class="stripe ${activeColor}"></div><i class="material-icons">${icon}</i>${text}</a></li>
    <#else>
        <li><a href="${link}" class="waves-effect"><i class="material-icons">${icon}</i>${text}</a></li>
    </#if>
</#macro>

<#macro itemUpdate link text icon>
    <#import "/spring.ftl" as s>
    <li><a href="<@s.url '${link}'/>" class="waves-effect budgetmaster-update budgetmaster-text-update"><i class="material-icons" id="icon-update">${icon}</i>${text}</a></li>
</#macro>

<#macro itemDebug text icon>
    <li><a class="waves-effect budgetmaster-red budgetmaster-text-update"><i class="material-icons" id="icon-update">${icon}</i>${text}</a></li>
</#macro>

<#macro subListStart ID text icon activeColor activeID>
<li>
    <ul class="collapsible collapsible-accordion no-padding sidenav-sub">
        <li>
            <#if activeID?starts_with(ID)>
                <a href="<@s.url '/charts'/>" class="collapsible-header no-padding active"><div class="stripe ${activeColor}"></div><i class="material-icons">${icon}</i>${text}</a>
            <#else>
                <a href="<@s.url '/charts'/>" class="collapsible-header nav-padding"><i class="material-icons">${icon}</i>${text}</a>
            </#if>
            <div class="collapsible-body">
                <ul class="sidenav-sub">
</#macro>

<#macro subListEnd>
                </ul>
            </div>
        </li>
    </ul>
</li>
</#macro>