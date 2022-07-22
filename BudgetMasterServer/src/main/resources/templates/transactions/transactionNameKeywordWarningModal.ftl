<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<div id="modalTransactionNameKeywordWarning" class="modal background-color">
    <div class="modal-content">
        <h4>${locale.getString("transaction.warning.name.keyword.title")}</h4>

        <div class="row">
            <div class="sol s12">
                ${locale.getString("transaction.warning.name.keyword.description.part1")}
                <span id="keyword">${keyword}</span>
                ${locale.getString("transaction.warning.name.keyword.description.part2")}
            </div>
        </div>

        <br>

        <div class="row center-align">
            <div class="col s12">
                <@header.buttonLink id='keyword-warning-button-ignore' url='' icon='save' localizationKey='transaction.warning.name.keyword.button.ignore' color='red' classes='text-white' noUrl=true/>
            </div>
        </div>
        <div class="row center-align">
            <div class="col s12">
                <@header.buttonLink url='' icon='edit' localizationKey='transaction.warning.name.keyword.button.cancel' color='green' id='buttonChangeTransactionType' classes='modal-action modal-close text-white' noUrl=true/>
            </div>
        </div>
        <div class="row center-align">
            <div class="col s12">
                <@header.buttonLink url='/settings' icon='rule_folder' localizationKey='transaction.warning.name.keyword.button.edit' color='blue' id='buttonChangeTransactionType' classes='text-white'/>
            </div>
        </div>
    </div>
</div>
