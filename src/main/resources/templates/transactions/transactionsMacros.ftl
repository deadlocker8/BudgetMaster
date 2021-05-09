<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<#macro transactionType transaction>
    <div class="col s1 l1 xl1">
        <#if transaction.isRepeating()>
            <i class="material-icons ">repeat</i>
        <#elseif transaction.isTransfer()>
            <i class="material-icons">swap_horiz</i>
        <#else>
            <i class="material-icons invisible">repeat</i>
        </#if>
    </div>
</#macro>

<#macro transactionCategory transaction alignment>
    <#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>
    <div class="col s2 l1 xl1 ${alignment}">
        <div class="hide-on-med-and-down">
            <@categoriesFunctions.categoryCircle transaction.category/>
        </div>
        <div class="hide-on-large-only">
            <@categoriesFunctions.categoryCircle transaction.category "category-circle-small"/>
        </div>
    </div>
</#macro>

<#macro transactionNameAndDescription transaction size>
    <div class="col ${size}">
        <div class="truncate transaction-text">${transaction.name}</div>
        <div class="hide-on-med-and-down">
            <#if transaction.description??>
                <div class="italic">${transaction.description}</div>
            </#if>
        </div>
    </div>
</#macro>

<#macro transactionAmount transaction account size>
    <#assign amount = helpers.getAmount(transaction, account)/>
    <#if amount <= 0>
        <div class="col ${size} bold ${redTextColor} no-wrap right-align transaction-text">${currencyService.getCurrencyString(amount)}</div>
    <#else>
        <div class="col ${size} bold ${greenTextColor} no-wrap right-align transaction-text">${currencyService.getCurrencyString(amount)}</div>
    </#if>
</#macro>

<#macro transactionButtons transaction>
        <div class="col s8 l2 xl1 right-align transaction-buttons no-wrap">
            <#if (transaction.category.type.name() != "REST") && transaction.getAccount().getAccountState().name() == "FULL_ACCESS">
                <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/edit' icon='edit' localizationKey='' classes="no-padding text-default"/>
                <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/requestDelete' icon='delete' localizationKey='' classes="no-padding text-default"/>
            </#if>
        </div>
</#macro>

<#macro transactionAccountIcon transaction>
    <#if helpers.getCurrentAccount().getType().name() == "ALL" && transaction.getAccount()??>
        <#import "../helpers/customSelectMacros.ftl" as customSelectMacros>
        <div class="col s2 l1 xl1 tooltipped no-padding" data-position="bottom" data-tooltip="${transaction.getAccount().getName()}">
            <div class="hide-on-med-and-down">
                <@customSelectMacros.accountIcon transaction.getAccount() transaction.getAccount().getName()/>
            </div>
            <div class="hide-on-large-only">
                <@customSelectMacros.accountIcon transaction.getAccount() transaction.getAccount().getName() "category-circle-small"/>
            </div>
        </div>
    </#if>

</#macro>
<#macro transactionLinks transaction>
    <div class="col s4 l2 xl1 right-align transaction-buttons no-wrap">
        <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/highlight' icon='open_in_new' localizationKey='' classes="no-padding text-default buttonHighlight"/>
        <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/edit' icon='edit' localizationKey='' classes="no-padding text-default"/>
    </div>
</#macro>


<#macro placeholder transactions>
    <#assign isOnlyRest = transactions?size == 1 && transactions[0].category.type.name() == "REST"/>
    <#if isOnlyRest>
        <br>
    </#if>

    <#if transactions?size == 0 || isOnlyRest>
        <div class="row">
            <div class="col s12">
                <div class="headline center-align">${locale.getString("placeholder.seems.empty")}</div>
                <div class="headline-advice center-align">${locale.getString("placeholder.advice", locale.getString("menu.transactions"))}</div>
            </div>
        </div>
    </#if>
</#macro>

<#macro buttons isFilterActive>
    <div class="row hide-on-small-only valign-wrapper">
        <div class="col s6 right-align transactions-buttons-col">
            <@buttonNew "new-transaction-button-list new-transaction-button-list-large"/>
        </div>
        <div class="col s6 left-align">
            <@buttonFilter isFilterActive/>
        </div>
    </div>

    <div class="hide-on-med-and-up center-align">
        <div class="row center-align">
            <div class="row center-align">
                <div class="col s12">
                    <@buttonFilter isFilterActive/>
                </div>
            </div>
            <div class="col s12 transactions-buttons-col">
                <@buttonNew "new-transaction-button-list "/>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonNew listClasses>
    <div class="fixed-action-btn new-transaction-button">
        <a class="btn-floating btn-large btn waves-effect waves-light background-blue" id="button-new-transaction">
            <i class="material-icons left">add</i>${locale.getString("title.transaction.new.short")}
        </a>
        <ul class="${listClasses}">
            <li>
                <a href="<@s.url '/templates'/>" class="btn-floating btn background-blue-baby"><i class="material-icons">file_copy</i></a>
                <a href="<@s.url '/templates'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new.from.template")}</a>
            </li>
            <li>
                <a href="<@s.url '/transactions/newTransaction/transfer'/>" class="btn-floating btn background-green-dark"><i class="material-icons">swap_horiz</i></a>
                <a href="<@s.url '/transactions/newTransaction/transfer'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new.transfer")}</a>
            </li>
            <li>
                <a href="<@s.url '/transactions/newTransaction/repeating'/>" class="btn-floating btn background-blue"><i class="material-icons">repeat</i></a>
                <a href="<@s.url '/transactions/newTransaction/repeating'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new.repeating")}</a>
            </li>
            <li>
                <a href="<@s.url '/transactions/newTransaction/normal'/>" class="btn-floating btn background-orange"><i class="material-icons">payment</i></a>
                <a href="<@s.url '/transactions/newTransaction/normal'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new.normal")}</a>
            </li>
        </ul>
    </div>
</#macro>

<#macro buttonFilter isFilterActive>
    <#if isFilterActive>
        <a href="#modalFilter" id="modalFilterTrigger" class="modal-trigger waves-effect waves-light btn background-red"><i class="fas fa-filter left"></i>${locale.getString("filter.active")}</a>
    <#else>
        <a href="#modalFilter" id="modalFilterTrigger" class="modal-trigger waves-effect waves-light btn background-blue"><i class="fas fa-filter left"></i>${locale.getString("title.filter")}</a>
    </#if>
</#macro>
