<#import "/spring.ftl" as s>

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
    <div class="col s3 l1 xl1 ${alignment}">
        <div class="hide-on-med-and-down">
            <div class="category-circle" style="background-color: ${transaction.category.color}">
                <span style="color: ${transaction.category.getAppropriateTextColor()}">
                    ${transaction.category.name?capitalize[0]}
                </span>
            </div>
        </div>
        <div class="hide-on-large-only">
            <div class="category-circle-small" style="background-color: ${transaction.category.color}">
                <span style="color: ${transaction.category.getAppropriateTextColor()}">
                    ${transaction.category.name?capitalize[0]}
                </span>
            </div>
        </div>
    </div>
</#macro>

<#macro transactionNameAndDescription transaction>
    <div class="col s5 l4 xl5">
        <div class="truncate transaction-text">${transaction.name}</div>
        <div class="hide-on-med-and-down">
            <#if transaction.description??>
                <div class="italic">${transaction.description}</div>
            </#if>
        </div>
    </div>
</#macro>

<#macro transactionAmount transaction account>
    <#assign amount = helpers.getAmount(transaction, account)/>
    <#if amount <= 0>
        <div class="col s4 l3 xl3 bold ${redTextColor} no-wrap right-align transaction-text">${helpers.getCurrencyString(amount)}</div>
    <#else>
        <div class="col s4 l3 xl3 bold ${greenTextColor} no-wrap right-align transaction-text">${helpers.getCurrencyString(amount)}</div>
    </#if>
</#macro>

<#macro transactionButtons transaction>
        <div class="col s8 l2 xl1 right-align transaction-buttons no-wrap">
            <#if (transaction.category.type.name() != "REST")>
                <a href="<@s.url '/transactions/${transaction.ID?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                <a href="<@s.url '/transactions/${transaction.ID?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left no-margin">delete</i></a>
            </#if>
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

<#macro deleteModal transaction>
    <div id="modalConfirmDelete" class="modal background-color">
        <div class="modal-content">
            <h4>${locale.getString("info.title.transaction.delete")}</h4>
            <#if currentTransaction.isRepeating()>
                <p>${locale.getString("info.text.transaction.repeating.delete", transaction.name)}</p>
            <#else>
                <p>${locale.getString("info.text.transaction.delete", transaction.name)}</p>
            </#if>
        </div>
        <div class="modal-footer background-color">
            <a href="<@s.url '/transactions'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
            <a href="<@s.url '/transactions/${transaction.ID?c}/delete'/>" class="modal-action modal-close waves-effectwaves-light green btn-flat white-text">${locale.getString("delete")}</a>
        </div>
    </div>
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
        <a class="btn-floating btn-large btn waves-effect waves-light budgetmaster-blue" id="button-new-transaction">
            <i class="material-icons left">add</i>${locale.getString("title.transaction.new.short")}
        </a>
        <ul class="${listClasses}">
            <li>
                <a href="<@s.url '/transactions/newTransaction/transfer'/>" class="btn-floating btn budgetmaster-dark-green"><i class="material-icons">swap_horiz</i></a>
                <a href="<@s.url '/transactions/newTransaction/transfer'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new.transfer")}</a>
            </li>
            <li>
                <a href="<@s.url '/transactions/newTransaction/repeating'/>" class="btn-floating btn budgetmaster-blue"><i class="material-icons">repeat</i></a>
                <a href="<@s.url '/transactions/newTransaction/repeating'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new.repeating")}</a>
            </li>
            <li>
                <a href="<@s.url '/transactions/newTransaction/normal'/>" class="btn-floating btn budgetmaster-orange"><i class="material-icons">payment</i></a>
                <a href="<@s.url '/transactions/newTransaction/normal'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new.normal")}</a>
            </li>
        </ul>
    </div>
</#macro>

<#macro buttonFilter isFilterActive>
    <#if isFilterActive>
        <a href="#modalFilter" class="modal-trigger waves-effect waves-light btn budgetmaster-red"><i class="fas fa-filter left"></i>${locale.getString("filter.active")}</a>
    <#else>
        <a href="#modalFilter" class="modal-trigger waves-effect waves-light btn budgetmaster-blue"><i class="fas fa-filter left"></i>${locale.getString("title.filter")}</a>
    </#if>
</#macro>
