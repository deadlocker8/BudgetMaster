<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<#macro imageSelect id item>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="fas fa-icons prefix"></i>
            <label class="input-label" for="${id}">${locale.getString("account.new.label.icon")}</label>

            <#assign hasImageIcon=item.getIconReference()?? && item.getIconReference().getImage()??/>
            <#assign hasBuiltinIcon=item.getIconReference()?? && item.getIconReference().getBuiltinIdentifier()??/>

            <div id="${id}" class="valign-wrapper item-icon">
                <a id="item-icon-preview" data-url="<@s.url '/media/getAvailableImages'/>">
                    <i id="builtin-icon-preview-icon" class="<#if hasBuiltinIcon>${item.getIconReference().getBuiltinIdentifier()}<#else>hidden</#if>"></i>
                    <img id="item-icon-preview-icon" src="<#if hasImageIcon>${item.getIconReference().getImage().getBase64EncodedImage()}</#if>" class="item-icon-preview <#if hasImageIcon == false>hidden</#if>"/>
                    <div id="item-icon-placeholder" class="<#if hasImageIcon || hasBuiltinIcon>hidden</#if>">${locale.getString("account.new.icon.placeholder")}</div>
                </a>
                <@header.buttonFlat url='' icon='delete' id='' localizationKey='' classes="no-padding text-default button-remove-icon-from-item" noUrl=true/>

                <input id="hidden-input-icon-image-id" type="hidden" name="iconImageID" value="<#if hasImageIcon>${item.getIconReference().getImage().getID()?c}</#if>">
                <input id="hidden-input-icon-builtin-identifier" type="hidden" name="builtinIconIdentifier" value="<#if hasBuiltinIcon>${item.getIconReference().getBuiltinIdentifier()}</#if>">
            </div>
        </div>
    </div>
</#macro>


<#macro modalIconSelect idToFocusOnClose item>
    <#assign hasBuiltinIcon=item.getIconReference()?? && item.getIconReference().getBuiltinIdentifier()??/>

    <div id="modalIconSelect" class="modal modal-fixed-footer background-color" data-focus-on-close="${idToFocusOnClose}">
        <div class="modal-content center-align">
            <div class="row">
                <div class="col s12">
                    <ul class="tabs" id="iconTabs">
                        <li class="tab col s6"><a class="text-blue valign-wrapper <#if hasBuiltinIcon == false>active</#if>" href="#tabImages" data-name="images"><i class="fas fa-image"></i> ${locale.getString(("icons.images"))}</a></li>
                        <li class="tab col s6"><a class="text-blue valign-wrapper <#if hasBuiltinIcon>active</#if>" href="#tabBuiltinIcons" data-name="builtinIcons"><i class="fas fa-icons"></i> ${locale.getString(("icons.builtin"))}</a></li>
                    </ul>
                </div>
                <div id="tabImages" class="col s12"><@tabImages/></div>
                <div id="tabBuiltinIcons" class="col s12"><@tabBuiltinIcons/></div>
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

<#macro tabBuiltinIcons>
    <div class="row no-margin-bottom">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="material-icons prefix">search</i>
            <input id="searchIcons" type="text" onchange="searchBuiltinIcons();" onkeypress="searchBuiltinIcons();" onpaste="searchBuiltinIcons()" oninput="searchBuiltinIcons();">
            <label for="searchIcons">${locale.getString("search")}</label>
        </div>
    </div>
    <div class="row">
        <div class="col s12 center-align" id="numberOfIcons"><span id="numberOfMatchingIcons">${fontawesomeIcons?size?c}</span>/${fontawesomeIcons?size?c} ${locale.getString("icons.numberOf")}</div>
    </div>

    <hr>

    <div class="row">
        <#list fontawesomeIcons as icon>
            <@builtinIconOption icon/>
        </#list>
    </div>
</#macro>

<#macro builtinIconOption icon>
    <div class="col s4 m2 l2 builtin-icon-option-column">
        <div class="builtin-icon-option">
            <i class="builtin-icon-option-icon ${icon}"></i>
            <div class="builtin-icon-option-name truncate">${icon}</div>
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