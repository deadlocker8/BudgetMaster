<#import "../helpers/header.ftl" as header>

<#macro modalAccountIconSelect>
    <div id="modalAccountIconSelect" class="modal modal-fixed-footer background-color">
        <div class="modal-content">
            <div class="row">
                <#list availableImages as image>
                    <@accountIconOption image/>
                </#list>
            </div>
        </div>
        <div class="modal-footer background-color">
            <@header.buttonLink url='' icon='save' localizationKey='cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
            <@header.buttonLink url='' icon='done' id='button-account-icon-confirm' localizationKey='ok' color='green' classes='modal-action modal-close text-white' noUrl=true/>
        </div>
    </div>
</#macro>

<#macro accountIconOption image>
    <div class="col s4 m2 l2 account-icon-option-column">
        <div class="account-icon-option">
            <img src="${image.getImagePath()}" class="account-icon-preview" alt="${image.getImagePath()}" data-image-id="${image.getID()}"/>
        </div>
    </div>
</#macro>