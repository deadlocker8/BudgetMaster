<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "helpers/header.ftl" as header>
<#import "helpers/customSelectMacros.ftl" as customSelectMacros>


<div id="modalGlobalAccountSelect" class="modal modal-fixed-footer background-color-light">
    <div class="modal-content">
        <div class="row no-margin-bottom">
            <div class="col s12">
                <h4>${locale.getString("account.select")}</h4>
            </div>
        </div>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonLink url='/accounts' icon='edit' localizationKey='account.button.manage'/>
            </div>
        </div>

        <div class="row">
            <#list accounts as account>
                <#if account.getType().name() == "ALL">
                    <#assign accountName=locale.getString("account.all")/>
                <#else>
                    <#assign accountName=account.getName()/>
                </#if>

                <div class="col s12 m6 xl6">
                    <a href="<@s.url '/accounts/${account.getID()?c}/select'/>" class="text-default">
                        <div class="card-panel global-account-select-option">
                            <@customSelectMacros.accountIcon account accountName "category-circle-preview account-icon-big"/>
                            <div class="global-account-select-option-name truncate">${accountName}</div>
                        </div>
                    </a>
                </div>
            </#list>
        </div>
    </div>
    <div class="modal-footer background-color-light">
        <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' id='buttonCloseGlobalAccountSelect' classes='modal-action modal-close text-white' isDataUrl=false noUrl=true/>
    </div>
</div>

