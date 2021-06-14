<#import "/spring.ftl" as s>
<#import "header.ftl" as header>

<@header.globals/>

<#list availableImages as image>
    <@imageOption image 'item-icon' selectedImageID/>
</#list>

<#macro imageOption image classPrefix selectedImageID>
    <div class="col s4 m2 l2 ${classPrefix}-option-column">
        <div class="${classPrefix}-option <#if selectedImageID?? && selectedImageID==image.getID()>selected</#if>">
            <img src="${image.getBase64EncodedImage()}" class="${classPrefix}-preview" data-image-id="${image.getID()}"/>
        </div>
        <@header.buttonFlat url="/media/deleteImage/" + image.getID() icon='delete' localizationKey='delete.question' classes='no-padding text-default ' + classPrefix + '-option-delete' isDataUrl=true/>
    </div>
</#macro>