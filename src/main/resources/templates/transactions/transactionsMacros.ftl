<#import "/spring.ftl" as s>

<#macro transactionRepeating transaction>
    <div class="col s1 l1">
        <i class="material-icons <#if !transaction.isRepeating()>invisible</#if>">repeat</i>
    </div>
</#macro>

<#macro transactionCategory transaction>
    <div class="col s3 l1 center-align">
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
    <div class="col s5 l7">
        <div class="truncate transaction-text">${transaction.name}</div>
        <div class="hide-on-med-and-down">
            <#if transaction.description??>
                <div class="italic">${transaction.description}</div>
            </#if>
        </div>
    </div>
</#macro>

<#macro transactionAmount amount>
    <#if amount <= 0>
        <div class="col s4 bold ${redTextColor} no-wrap right-align transaction-text">${helpers.getCurrencyString(amount)}</div>
    <#else>
        <div class="col s4 bold ${greenTextColor} no-wrap right-align transaction-text">${helpers.getCurrencyString(amount)}</div>
    </#if>
</#macro>

<#macro transactionButtons transaction>
        <div class="col s8 l10 right-align transaction-buttons">
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
        <div class="col s6 right-align">
            <@buttonNew/>
        </div>

        <div class="col s6 left-align">
            <@buttonFilter isFilterActive/>
        </div>
    </div>

    <div class="hide-on-med-and-up valign-wrapper">
        <div class="row center-align">
            <div class="col s12">
                <@buttonNew/>
            </div>
        </div>
        <div class="row center-align">
            <div class="col s12">
                <@buttonFilter isFilterActive/>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonNew>
    <a href="<@s.url '/transactions/newTransaction'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.transaction.new")}</a>
</#macro>

<#macro buttonFilter isFilterActive>
    <#if isFilterActive>
        <a href="#modalFilter" class="modal-trigger waves-effect waves-light btn budgetmaster-red"><i class="fas fa-filter left"></i>${locale.getString("filter.active")}</a>
    <#else>
        <a href="#modalFilter" class="modal-trigger waves-effect waves-light btn budgetmaster-blue"><i class="fas fa-filter left"></i>${locale.getString("title.filter")}</a>
    </#if>
</#macro>
