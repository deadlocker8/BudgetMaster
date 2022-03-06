<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<@header.modalConfirmDelete title=locale.getString("info.title.transaction.delete") confirmUrl='/transactions' itemId=transactionToDelete.getID() confirmButtonTextKey='delete'>
    <#if transactionToDelete.isRepeating()>
        <p>${locale.getString("info.text.transaction.repeating.delete", transactionToDelete.name)}</p>
    <#else>
        <p>${locale.getString("info.text.transaction.delete", transactionToDelete.name)}</p>
    </#if>
</@header.modalConfirmDelete>