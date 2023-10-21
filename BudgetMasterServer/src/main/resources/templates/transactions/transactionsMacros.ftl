<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<#macro transactionType transaction size>
    <div class="col ${size} left-align transaction-row-transparent-child">
        <#if transaction.isRepeating()>
            <i class="material-icons">repeat</i>
        </#if>
        <#if transaction.isTransfer()>
            <i class="material-icons tooltipped" data-position="bottom" data-tooltip="${transaction.getAccount().getName()} ➔ ${transaction.getTransferAccount().getName()}">swap_horiz</i>
        </#if>

        <#if !transaction.isRepeating() && !transaction.isTransfer()>
            <i class="material-icons invisible">repeat</i>
        </#if>
    </div>
</#macro>

<#macro transactionCategory transaction alignment>
    <#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>
    <div class="col s2 l1 xl1 ${alignment} transaction-row-transparent-child">
        <div class="hide-on-med-and-down">
            <@categoriesFunctions.categoryCircle category=transaction.category enableSearchWrapper=true/>
        </div>
        <div class="hide-on-large-only">
            <@categoriesFunctions.categoryCircle category=transaction.category classes="category-circle-small" enableSearchWrapper=true/>
        </div>
    </div>
</#macro>

<#macro transactionNameAndDescription transaction size>
    <div class="col ${size} transaction-row-transparent-child">
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
        <div class="col ${size} bold ${redTextColor} no-wrap right-align transaction-text transaction-row-transparent-child">${currencyService.getCurrencyString(amount)}</div>
    <#else>
        <div class="col ${size} bold ${greenTextColor} no-wrap right-align transaction-text transaction-row-transparent-child">${currencyService.getCurrencyString(amount)}</div>
    </#if>
</#macro>

<#macro transactionButtons transaction classes>
        <div class="col ${classes} transaction-buttons no-wrap">
            <#if transaction.isEditable()>
                <@transactionEditButton transaction/>
                <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/requestDelete' icon='delete' localizationKey='' classes="no-padding text-default button-request-delete-transaction" isDataUrl=true/>
            </#if>
            <#if transaction.isAllowedToFillNewTransaction()>
                <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/newFromExisting' icon='content_copy' localizationKey='' classes="no-padding text-default button-new-from-existing"/>
            </#if>
        </div>
</#macro>

<#macro transactionEditButton transaction>
    <#if transaction.isRepeating()>
        <div class="fixed-action-btn edit-transaction-button">
            <a class="btn-floating btn-flat waves-effect waves-light no-padding text-default edit-transaction-button-link">
                <i class="material-icons">edit</i>
            </a>
            <ul class="new-transaction-button-list">
                <li>
                    <a href="<@s.url '/transactions/' + transaction.ID?c + '/edit'/>" class="btn-floating btn mobile-fab-tip no-wrap button-edit-all-occurrences">${locale.getString("title.transaction.edit", locale.getString("title.transaction.new.normal"))}</a>
                    <a href="<@s.url '/transactions/' + transaction.ID?c + '/edit'/>" class="btn-floating btn background-green-dark"><i class="material-icons">edit</i></a>
                </li>
                <li>
                    <a href="<@s.url '/transactions/' + transaction.ID?c + '/editFutureRepetitions'/>" class="btn-floating btn mobile-fab-tip no-wrap button-edit-future-occurrences">${locale.getString("repeating.button.edit.future")}</a>
                    <a href="<@s.url '/transactions/' + transaction.ID?c + '/editFutureRepetitions'/>" class="btn-floating btn background-orange"><i class="material-icons">move_up</i></a>
                </li>
            </ul>
        </div>
    <#else>
        <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/edit' icon='edit' localizationKey='' classes="no-padding text-default button-edit"/>
    </#if>
</#macro>

<#macro transactionAccountIcon transaction>
    <#if helpers.getCurrentAccount().getType().name() == "ALL" && transaction.getAccount()??>
        <#import "../helpers/customSelectMacros.ftl" as customSelectMacros>
        <a href="<@s.url '/accounts/' + transaction.getAccount().getID() + '/select'/>">
            <div class="col s2 l1 xl1 tooltipped no-padding transaction-row-transparent-child" data-position="bottom" data-tooltip="${transaction.getAccount().getName()}">
                <div class="hide-on-med-and-down">
                    <@customSelectMacros.accountIcon transaction.getAccount() transaction.getAccount().getName()/>
                </div>
                <div class="hide-on-large-only">
                    <@customSelectMacros.accountIcon transaction.getAccount() transaction.getAccount().getName() "category-circle-small"/>
                </div>
            </div>
        </a>
    </#if>

</#macro>
<#macro transactionLinks transaction target=''>
    <div class="col s4 l2 xl1 right-align transaction-buttons no-wrap">
        <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/highlight' icon='open_in_new' localizationKey='' classes="no-padding text-default buttonHighlight" target=target/>
        <#if transaction.getAccount().getAccountState().name() == 'FULL_ACCESS'>
            <@header.buttonFlat url='/transactions/' + transaction.ID?c + '/edit' icon='edit' localizationKey='' classes="no-padding text-default" target=target/>
        </#if>
    </div>
</#macro>


<#macro placeholder transactions>
    <br>

    <#assign isOnlyRest = transactions?size == 2 && transactions[0].category.type.name() == "REST" && transactions[1].category.type.name() == "REST"/>
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
        <#if helpers.getCurrentAccount().getAccountState().name() == 'FULL_ACCESS'>
            <div class="col s6 right-align transactions-buttons-col">
                <@buttonNew "new-transaction-button-list new-transaction-button-list-large" helpers.getCurrentAccount()/>
            </div>
            <div class="col s6 left-align">
                <@buttonFilter isFilterActive/>
            </div>
        <#else>
            <div class="col s12 center-align">
                <@buttonFilter isFilterActive/>
            </div>
        </#if>
    </div>

    <div class="hide-on-med-and-up center-align">
        <div class="row center-align">
            <div class="row center-align">
                <div class="col s12">
                    <@buttonFilter isFilterActive/>
                </div>
            </div>
            <div class="col s12 transactions-buttons-col">
                <@buttonNew "new-transaction-button-list " helpers.getCurrentAccount()/>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonNew listClasses currentAccount>
    <#if currentAccount.getAccountState().name() != 'FULL_ACCESS'>
        <#return/>
    </#if>

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
