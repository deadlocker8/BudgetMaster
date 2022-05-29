<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<@header.modalConfirmDelete title=locale.getString("info.title.template.delete") confirmUrl='/templates' itemId=templateToDelete.getID() confirmButtonTextKey='info.title.template.delete'>
    <p>${locale.getString("info.text.template.delete", templateToDelete.getTemplateName())}</p>
</@header.modalConfirmDelete>