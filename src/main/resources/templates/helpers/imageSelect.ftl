<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<#macro imageSelect id item>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="fas fa-icons prefix"></i>
            <label class="input-label" for="${id}">${locale.getString("account.new.label.icon")}</label>

            <div id="${id}" class="valign-wrapper item-icon">
                <a id="item-icon-preview" data-url="<@s.url '/media/getAvailableImages'/>">
                    <img id="item-icon-preview-icon" src="<#if item.getIconReference()?? && item.getIconReference().getImage()??>${item.getIconReference().getImage().getBase64EncodedImage()}</#if>" class="item-icon-preview <#if item.getIconReference()?? && item.getIconReference().getImage()?? == false>hidden</#if>"/>
                    <div id="item-icon-placeholder" class="<#if item.getIconReference()?? && item.getIconReference().getImage()??>hidden</#if>">${locale.getString("account.new.icon.placeholder")}</div>
                </a>
                <@header.buttonFlat url='' icon='delete' id='' localizationKey='' classes="no-padding text-default button-remove-icon-from-item" noUrl=true/>

                <input id="hidden-input-icon-image-id" type="hidden" name="iconImageID" value="<#if item.getIconReference()?? && item.getIconReference().getImage()??>${item.getIconReference().getImage().getID()?c}</#if>">
            </div>
        </div>
    </div>
</#macro>


<#macro modalIconSelect idToFocusOnClose>
    <div id="modalIconSelect" class="modal modal-fixed-footer background-color" data-focus-on-close="${idToFocusOnClose}">
        <div class="modal-content center-align">
            <div class="row">
                <div class="col s12">
                    <ul class="tabs">
                        <li class="tab col s6"><a class="text-blue valign-wrapper active" href="#tabImages"><i class="fas fa-image"></i> Custom Images</a></li>
                        <li class="tab col s6"><a class="text-blue valign-wrapper" href="#tabBuiltinIcons"><i class="fas fa-icons"></i> Built-in Icons</a></li>
                    </ul>
                </div>
                <div id="tabImages" class="col s12"><@tabImages/></div>
                <div id="tabBuiltinIcons" class="col s12">Built-in Icons</div>
            </div>
        </div>
        <div class="modal-footer background-color">
            <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
            <@header.buttonLink url='' icon='done' id='button-icon-confirm' localizationKey='ok' color='green' classes='modal-action modal-close text-white' noUrl=true disabled=true/>
        </div>
    </div>
</#macro>

<#macro tabImages>
     <div class="row">
        <div class="col s12">
            <div class="headline">${locale.getString('upload.image.headline')}</div>
        </div>
    </div>

    <div class="row">
        <@uploadImageForm/>
    </div>

    <hr>

    <div class="row">
        <div class="col s12">
            <div class="headline">${locale.getString('available.images')}</div>
        </div>
    </div>

    <div class="row" id="available-images">
    </div>
</#macro>

<#macro uploadImageForm>
    <form id="form-upload-image" method="post" action="<@s.url '/media/uploadImage'/>" enctype="multipart/form-data">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="file-field input-field col s12">
            <div class="container">
                <div class="btn background-blue">
                    <i class="material-icons left">folder</i>
                    ${locale.getString("account.new.icon.upload.choose.file")}
                    <input id="inputUploadFile" type="file" accept="${helpers.getValidImageUploadTypes()}" name="file">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text">
                </div>
                <@header.buttonLink url='' icon='upload' localizationKey='account.new.icon.upload' id='button-upload-new-image' classes='right' noUrl=true/>
            </div>
        </div>
    </form>
</#macro>