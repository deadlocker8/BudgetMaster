<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "helpers/header.ftl" as header>

<div id="modalGlobalAccountSelect" class="modal modal-fixed-footer background-color">
    <div class="modal-content">
        <div class="row">
            <div class="col s12">
                <h3>${locale.getString("account.select")}</h3>
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <#list accounts as account>
                    ${account.getName()}<br>
                </#list>
            </div>
        </div>
    </div>
    <div class="modal-footer background-color">
        <@header.buttonLink url='' icon='cancel' localizationKey='cancel' color='red' id='buttonCloseGlobalAccountSelect' classes='modal-action modal-close text-white' isDataUrl=false noUrl=true/>
    </div>
</div>

