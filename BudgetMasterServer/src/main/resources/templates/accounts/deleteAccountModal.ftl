<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<@header.modalConfirmDelete title=locale.getString("info.title.account.delete") confirmUrl='/accounts' itemId=accountToDelete.getID() confirmButtonTextKey="info.button.account.delete">
    <p>${locale.getString("info.text.account.delete", accountToDelete.getName(), accountToDelete.getReferringTransactions()?size)}</p>
</@header.modalConfirmDelete>