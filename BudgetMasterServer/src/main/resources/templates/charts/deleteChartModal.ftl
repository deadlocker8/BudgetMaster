<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<@header.modalConfirmDelete title=locale.getString("info.title.chart.delete") confirmUrl='/charts' itemId=chartToDelete.getID() confirmButtonTextKey='info.title.chart.delete'>
    <p>${locale.getString("info.text.chart.delete", chartToDelete.getName())}</p>
</@header.modalConfirmDelete>