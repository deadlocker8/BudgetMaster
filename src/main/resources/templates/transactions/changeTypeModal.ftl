<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<div id="modalChangeTransactionType" class="modal background-color">
    <div class="modal-content">
        <h4>${locale.getString("transaction.change.type")}</h4>

        <div class="row">
            <div class="sol s12">
                ${locale.getString("transaction.change.type.warning")}
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <select id="newTypeSelect">
                    <#if transaction.isRepeating() || transaction.isTransfer()>
                        <option value="1">${locale.getString("title.transaction.new.normal")}</option>
                    </#if>
                    <#if !transaction.isRepeating()>
                        <option value="2">${locale.getString("title.transaction.new.repeating")}</option>
                    </#if>
                    <#if !transaction.isTransfer()>
                        <option value="3">${locale.getString("title.transaction.new.transfer")}</option>
                    </#if>
                </select>
                <label for="newTypeSelect">${locale.getString("transaction.change.type.new")}</label>
            </div>
        </div>
    </div>
    <div class="modal-footer background-color">
        <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white'/>
        <@header.buttonLink url='' icon='shuffle' localizationKey='ok' color='green' id='buttonChangeTransactionType' classes='"modal-action modal-close text-white' noUrl=true/>
    </div>

    <form id="formChangeTransactionType" class="hidden" action="<@s.url '/transactions/${transaction.getID()?c}/changeType'/>">
        <input type="hidden" name="newType" id="inputNewType">
    </form>
</div>