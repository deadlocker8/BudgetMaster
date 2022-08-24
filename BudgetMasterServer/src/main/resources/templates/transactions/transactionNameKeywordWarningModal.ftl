<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<div id="modalTransactionNameKeywordWarning" class="modal background-color">
    <div class="modal-content">
        <h4>${locale.getString("transaction.warning.name.keyword.title")}</h4>

        <div class="row">
            <div class="sol s12">
                ${locale.getString("transaction.warning.name.keyword.description.part1")}
                <span id="keyword">${transactionNameKeyword}</span>
                ${locale.getString("transaction.warning.name.keyword.description.part2")}
            </div>
        </div>
    </div>

    <div class="modal-footer background-color">
        <div class="left">
            <@header.buttonLink url='/settings' icon='rule_folder' localizationKey='transaction.warning.name.keyword.button.edit' color='background-blue' classes='text-white modal-' target='_blank'/>
        </div>
        <@header.buttonLink id='keyword-warning-button-cancel' url='' icon='clear' localizationKey='transaction.warning.name.keyword.button.cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
        <@header.buttonLink id='keyword-warning-button-ignore' url='' icon='save' localizationKey='transaction.warning.name.keyword.button.ignore' color='green' classes='text-white' noUrl=true/>
    </div>
</div>
