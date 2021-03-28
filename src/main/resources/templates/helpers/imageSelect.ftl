<#import "/spring.ftl" as s>

<#macro imageSelect id item>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="fas fa-icons prefix"></i>
            <label class="input-label" for="${id}">${locale.getString("account.new.label.icon")}</label>

            <div id="${id}" class="valign-wrapper">
                <a id="${id}-preview" data-url="<@s.url '/media/getAvailableImages'/>">
                    <img id="${id}-preview-icon" src="<#if item.getIcon()??>${item.getIcon().getBase64EncodedImage()}</#if>" class="${id}-preview <#if item.getIcon()?? == false>hidden</#if>"/>
                    <div id="${id}-placeholder" class="<#if item.getIcon()??>hidden</#if>">${locale.getString("account.new.icon.placeholder")}</div>
                </a>
                <@header.buttonFlat url='' icon='delete' id='button-remove-${id}' localizationKey='' classes="no-padding text-default" noUrl=true/>

                <input id="hidden-input-${id}" type="hidden" name="icon" value="<#if item.getIcon()??>${item.getIcon().getID()?c}</#if>">
            </div>
        </div>
    </div>
</#macro>