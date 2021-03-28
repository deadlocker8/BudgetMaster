<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<#macro imageSelect id item>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="fas fa-icons prefix"></i>
            <label class="input-label" for="${id}">${locale.getString("account.new.label.icon")}</label>

            <div id="${id}" class="valign-wrapper">
                <a id="item-icon-preview" data-url="<@s.url '/media/getAvailableImages'/>">
                    <img id="item-icon-preview-icon" src="<#if item.getIcon()??>${item.getIcon().getBase64EncodedImage()}</#if>" class="item-icon-preview <#if item.getIcon()?? == false>hidden</#if>"/>
                    <div id="item-icon-placeholder" class="<#if item.getIcon()??>hidden</#if>">${locale.getString("account.new.icon.placeholder")}</div>
                </a>
                <@header.buttonFlat url='' icon='delete' id='' localizationKey='' classes="no-padding text-default button-remove-icon-from-item" noUrl=true/>

                <input id="hidden-input-icon" type="hidden" name="icon" value="<#if item.getIcon()??>${item.getIcon().getID()?c}</#if>">
            </div>
        </div>
    </div>
</#macro>


<#macro modalIconSelect>
    <div id="modalIconSelect" class="modal modal-fixed-footer background-color">
        <div class="modal-content center-align">
            <div class="row">
                <div class="col s12">
                    <div class="headline">Upload image</div>
                </div>
            </div>

            <div class="row">
                <@uploadImageForm/>
            </div>

            <hr>

            <div class="row">
                <div class="col s12">
                    <div class="headline">Available images</div>
                </div>
            </div>

            <div class="row" id="available-images">
            </div>
        </div>
        <div class="modal-footer background-color">
            <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
            <@header.buttonLink url='' icon='done' id='button-icon-confirm' localizationKey='ok' color='green' classes='modal-action modal-close text-white' noUrl=true/>
        </div>
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