<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>

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
                    <span class="columnName-checkbox-label text-color">${locale.getString('template.checkbox.include.category')}</span>
                </label>
            </div>
            <div class="col s12">
                <label>
                    <input id="include-account" type="checkbox">
                    <span class="columnName-checkbox-label text-color">${locale.getString('template.checkbox.include.account')}</span>
                </label>
            </div>
        </div>
    </div>
    <div class="modal-footer background-color">
        <a class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
        <a id="buttonCreateTemplate" class="modal-action waves-effect waves-light green btn-flat white-text" data-url="<@s.url '/templates/fromTransaction'/>">${locale.getString("save.as.template")}</a>
    </div>

    <script>
        existingTemplateNames = ${existingTemplateNames};
    </script>
</div>