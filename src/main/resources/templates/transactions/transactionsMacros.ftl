<#import "/spring.ftl" as s>

<#macro transactionRepeating transaction>
    <td>
        <#if transaction.isRepeating()><i class="material-icons">repeat</i></#if>
    </td>
</#macro>

<#macro transactionCategory transaction>
    <td>
        <div class="category-circle" style="background-color: ${transaction.category.color}">
            <span style="color: ${transaction.category.getAppropriateTextColor()}">
                ${transaction.category.name?capitalize[0]}
            </span>
        </div>
    </td>
</#macro>

<#macro transactionNameAndDescription transaction>
    <td class="transaction-name">
        <div>${transaction.name}</div>
        <#if transaction.description??>
            <div class="italic">${transaction.description}</div>
        </#if>
    </td>
</#macro>

<#macro transactionAmount amount>
    <#if amount <= 0>
        <td class="bold ${redTextColor}">${helpers.getCurrencyString(amount)}</td>
    <#else>
        <td class="bold ${greenTextColor}">${helpers.getCurrencyString(amount)}</td>
    </#if>
</#macro>

<#macro transactionButtons transaction>
    <td>
        <#if (transaction.category.type.name() != "REST")>
            <a href="<@s.url '/transactions/${transaction.ID?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
            <a href="<@s.url '/transactions/${transaction.ID?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
        </#if>
    </td>
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