<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<div id="modalCreateFromTransaction" class="modal background-color">
    <div class="modal-content">
        <h4>${locale.getString("save.as.template")}</h4>

        <div class="row">
            <div class="input-field col s12">
                <input id="template-name" type="text" name="templateName">
                <label for="template-name">${locale.getString("transaction.new.label.name")}</label>
            </div>
            <div class="col s12">
                <label>
                    <input id="include-category" type="checkbox" checked="checked">
                    <span class="columnName-checkbox-label text-default">${locale.getString('template.checkbox.include.category')}</span>
                </label>
            </div>
            <div class="col s12">
                <label>
                    <input id="include-account" type="checkbox">
                    <span class="columnName-checkbox-label text-default">${locale.getString('template.checkbox.include.account')}</span>
                </label>
            </div>
        </div>
    </div>
    <div class="modal-footer background-color">
        <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white'/>
        <@header.buttonLink url='/templates/fromTransaction' icon='file_copy' localizationKey='save.as.template' color='green' id='buttonCreateTemplate' classes='"modal-action modal-close text-white' isDataUrl=true/>
    </div>

    <script>
        existingTemplateNames = ${existingTemplateNames};
    </script>
</div>