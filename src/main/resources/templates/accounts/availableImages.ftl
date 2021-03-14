<#list availableImages as image>
    <@imageOption image 'account-icon'/>
</#list>

<#macro imageOption image classPrefix>
    <div class="col s4 m2 l2 ${classPrefix}-option-column">
        <div class="${classPrefix}-option">
            <img src="${image.getBase64EncodedImage()}" class="${classPrefix}-preview" data-image-id="${image.getID()}"/>
        </div>
    </div>
</#macro>