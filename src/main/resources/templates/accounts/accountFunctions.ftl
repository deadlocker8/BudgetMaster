<#import "../helpers/header.ftl" as header>

<#macro modalAccountIconSelect>
    <div id="modalAccountIconSelect" class="modal modal-fixed-footer background-color">
        <div class="modal-content">
            <div class="row">
                <#list iconImages as image>
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

<#macro accountIconOption icon>
    <div class="col s4 m2 l2 account-icon-option-column">
        <div class="account-icon-option">
            <img src="${icon}" class="account-icon-preview" alt="${icon}" data-image-id="1"/>
        </div>
    </div>
</#macro>