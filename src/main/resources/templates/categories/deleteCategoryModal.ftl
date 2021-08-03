<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>
<#import "../helpers/customSelectMacros.ftl" as customSelectMacros>

<div id="modalConfirmDelete" class="modal categoryDeleteModal background-color">
    <div class="modal-content">
        <h4>${locale.getString("info.title.category.delete")}</h4>
        <p>${locale.getString("info.text.category.delete", categoryToDelete.name)}</p>
        <p>${locale.getString("info.text.category.delete.move")}</p>

        <form name="DestinationCategory" id="formDestinationCategory" action="<@s.url '/categories/${categoryToDelete.ID?c}/delete'/>" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <#import "../helpers/validation.ftl" as validation>
            <@customSelectMacros.customCategorySelect availableCategories preselectedCategory "col s12 m12 l8 offset-l2" locale.getString("info.title.category.delete.move")/>
        </form>
    </div>

    <div class="modal-footer background-color">
        <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
        <@header.buttonLink url='' icon='delete' localizationKey='delete' color='green' id='buttonDeleteCategory' classes='modal-action modal-close text-white' noUrl=true/>
    </div>
</div>