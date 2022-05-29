<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<@header.modalConfirmDelete title=locale.getString("info.title.template.group.delete") confirmUrl='/templateGroups' itemId=templateGroupToDelete.getID() confirmButtonTextKey='info.title.template.group.delete'>
    <p>${locale.getString("info.text.template.group.delete", templateGroupToDelete.getName())}</p>
</@header.modalConfirmDelete>