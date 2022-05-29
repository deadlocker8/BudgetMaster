<#import "/spring.ftl" as s>

<#macro navbar activeID settings>
    <ul id="slide-out" class="sidenav sidenav-fixed">
        <@itemLogo/>
        <@itemDivider/>
        <@itemSearch/>
        <@itemDivider/>
        <@itemAccountSelect/>
        <@itemDivider/>

        <@itemWithIcon "home", "/", locale.getString("menu.home"), entityType.HOME.getIcon(), entityType.HOME.getColor(), activeID/>
        <@itemWithIcon "transactions", "/transactions", locale.getString("menu.transactions"), entityType.TRANSACTION.getIcon(), entityType.TRANSACTION.getColor(), activeID/>
        <@itemWithIcon "templates", "/templates", locale.getString("menu.templates"), entityType.TEMPLATE.getIcon(), entityType.TEMPLATE.getColor(), activeID/>
        <@itemWithIcon "charts" "/charts" locale.getString("menu.charts"), entityType.CHART.getIcon(), entityType.CHART.getColor(), activeID/>
        <@itemWithIcon "reports", "/reports", locale.getString("menu.reports"), entityType.REPORT.getIcon(), entityType.REPORT.getColor(), activeID/>
        <@itemWithIcon "categories", "/categories", locale.getString("menu.categories"), entityType.CATEGORY.getIcon(), entityType.CATEGORY.getColor(), activeID/>
        <@itemWithIcon "tags", "/tags", locale.getString("menu.tags"), entityType.TAGS.getIcon(), entityType.TAGS.getColor(), activeID/>
        <@itemWithIcon "statistics", "/statistics", locale.getString("menu.statistics"), entityType.STATISTICS.getIcon(), entityType.STATISTICS.getColor(), activeID/>
        <@itemWithIcon "settings", "/settings", locale.getString("menu.settings"), entityType.SETTINGS.getIcon(), entityType.SETTINGS.getColor(), activeID/>

        <@itemDivider/>
        <@itemWithIcon "hotkeys", "/hotkeys", locale.getString("menu.hotkeys"), entityType.HOTKEYS.getIcon(), entityType.HOTKEYS.getColor(), activeID/>
        <@itemWithFontawesomeIcon "firstUseGuide", "/firstUse", locale.getString("menu.firstUseGuide"), "fas fa-graduation-cap", "background-grey", activeID/>
        <@itemWithIcon "about", "/about", locale.getString("menu.about"), entityType.ABOUT.getIcon(), entityType.ABOUT.getColor(), activeID/>

        <@itemDivider/>
        <@itemLogout locale.getString("menu.logout") "lock"/>

        <#if updateService.isUpdateAvailable()>
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
    <@migrationModal activeID/>

    <div id="globalAccountSelectModalOnDemand"></div>
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
                    <input id="search" class="mousetrap" name="searchText" type="search">
                    <label class="label-icon" for="search"><i class="material-icons">search</i></label>
                    <i id="buttonSearch" class="material-icons">send</i>
                    <i id="buttonClearSearch" class="material-icons">close</i>
                </div>
            </form>
        </div>
    </nav>
</#macro>

<#macro itemAccountSelect>
    <#import "customSelectMacros.ftl" as customSelectMacros>

    <a id="globalAccountSelect" class="center-align" data-url="<@s.url '/accounts/globalAccountSelectModal'/>">
        <#assign selectedAccount=helpers.getCurrentAccount()/>
        <#if selectedAccount.getType().name() == "ALL">
            <#assign accountName=locale.getString("account.all")/>
        <#else>
            <#assign accountName=selectedAccount.getName()/>
        </#if>

        <@customSelectMacros.accountIcon selectedAccount accountName "category-circle-preview account-icon-big"/>
        <div class="global-account-select-right">
            <div class="truncate global-account-select-name text-default">${accountName}</div>

            <div>
                <#assign accountBudget = helpers.getAccountBudget()/>
                <#if accountBudget <= 0>
                    <div class="global-account-select-budget ${redTextColor}">${currencyService.getCurrencyString(accountBudget)}</div>
                <#else>
                    <div class="global-account-select-budget ${greenTextColor}">${currencyService.getCurrencyString(accountBudget)}</div>
                </#if>
            </div>
        </div>
    </a>
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
                <@header.buttonLink url='/backupReminder/cancel' icon='clear' localizationKey='cancel' color='red' id='buttonCloseReminder' classes='modal-action modal-close text-white'/>
                <@header.buttonLink url='/backupReminder/settings' icon='settings' localizationKey='info.button.backup.reminder' color='green' id='buttonCloseReminder' classes='modal-action modal-close text-white'/>
            </div>
        </div>
    </#if>
</#macro>

<#macro whatsNewModal settings>
    <#if settings.needToShowWhatsNew()>
        <div id="whatsNewModelContainer" data-url="<@s.url '/about/whatsNewModal'/>"></div>
    </#if>
</#macro>

<#macro migrationModal loadedPage>
    <#if migrationService.needToShowMigrationDialog(loadedPage)>
        <div id="modalMigration" class="modal background-color">
            <div class="modal-content">
                <h4>${locale.getString("info.title.migration")}</h4>
                <p>${locale.getString("info.text.migration")}</p>
            </div>
            <div class="modal-footer background-color">
                <@header.buttonLink url='/migration/cancel' icon='clear' localizationKey='cancel' color='red' id='buttonCloseMigration' classes='modal-action modal-close text-white'/>
                <@header.buttonLink url='/migration' icon='merge' localizationKey='info.button.migration' color='green' id='buttonStartMigration' classes='modal-action modal-close text-white'/>
            </div>
        </div>
    </#if>
</#macro>