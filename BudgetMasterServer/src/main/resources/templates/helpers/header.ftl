<#macro style name>
    <#import "/spring.ftl" as s>
    <link type="text/css" rel="stylesheet" href="<@s.url '${"/css/" + name + ".css"}'/>"/>
</#macro>

<#macro globals>
    <#global locale = static["de.thecodelabs.utils.util.Localization"]>
    <#global programArgs = static["de.deadlocker8.budgetmaster.ProgramArgs"]>
    <#global entityType = static["de.deadlocker8.budgetmaster.services.EntityType"]>

    <#if helpers.getSettings().isUseDarkTheme()>
        <#global greenTextColor="text-green"/>
        <#global redTextColor="text-red-light"/>
    <#else>
        <#global greenTextColor="text-green-dark"/>
        <#global redTextColor="text-red"/>
    </#if>
</#macro>

<#macro header title>
    <#import "/spring.ftl" as s>
    <title>${title}</title>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="<@s.url '/webjars/font-awesome/6.4.2/css/all.min.css'/>">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="<@s.url "/webjars/materializecss/1.0.0/css/materialize.min.css"/>">
    <@style "colors"/>
    <@style "style"/>
    <@style "navbar"/>
    <@style "categories"/>
    <@style "accounts"/>
    <@style "customSelect"/>
    <@style "hotkeys"/>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link rel="apple-touch-icon" href="<@s.url "/touch_icon.png"/>">
</#macro>

<#macro logo id classes>
    <#import "/spring.ftl" as s>
    <#if helpers.getSettings().isUseDarkTheme()>
        <img class="${classes}" id="${id}" src="<@s.url '/images/Logo_with_white_text_medium_res.png'/>">
    <#else>
        <img class="${classes}" id="${id}" src="<@s.url '/images/Logo_with_text_medium_res.png'/>">
    </#if>
</#macro>

<#macro modalConfirmDelete title confirmUrl itemId confirmButtonTextKey id="modalConfirmDelete" classes="">
    <div id="${id}" class="modal background-color ${classes}">
        <div class="modal-content">
            <h4>${title}</h4>

            <#nested>
        </div>
        <div class="modal-footer background-color">
            <@buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
            <@buttonLink url=confirmUrl + '/' + itemId?c + '/delete' icon='delete' localizationKey=confirmButtonTextKey color='green' classes='modal-action modal-close text-white'/>
        </div>
    </div>
</#macro>

<#macro body>
    <body class="background-blue-light" <#if settings.isUseDarkTheme()>data-theme="dark"</#if>>
        <#nested>
    </body>
</#macro>

<#macro content>
    <#if notifications??>
        <@showNotifications notifications/>
    </#if>

    <#nested>
</#macro>

<#macro showNotifications notifications>
    <#list notifications as notification>
        <div class="row notification-row" id="notification-${notification?index}">
            <div class="col s12 center-align">
                <div class="notification-wrapper">
                    <div class="notification ${notification.getBackgroundColor()} ${notification.getTextColor()}">
                        <#if notification.getIcon()??>
                            <i class="${notification.getIcon()} notification-item"></i>
                        </#if>
                        <span class="notification-item">${notification.getMessage()}</span>
                        <a class="notification-item notification-clear ${notification.getTextColor()}" data-id="notification-${notification?index}">
                            <i class="material-icons">clear</i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </#list>
</#macro>

<#macro hint hint icon="fas fa-info" actionUrl="">
    <#if hint.isDismissed()>
        <#return>
    </#if>

    <div class="row" id="hint-${hint.getID()}">
        <div class="col s12 center-align">
            <div class="notification-wrapper">
                <div class="notification notification-border text-default">
                    <i class="${icon} notification-item"></i>
                    <#if actionUrl?has_content>
                        <a href="<@s.url actionUrl/>" class="text-default">
                    </#if>
                        <span class="notification-item left-align">${locale.getString(hint.getLocalizationKey())}</span>
                    <#if actionUrl?has_content>
                        </a>
                    </#if>
                    <a class="notification-item hint-clear text-default" data-url="<@s.url "/hints/dismiss/" + hint.getID()/>" data-id="hint-${hint.getID()}">
                        <i class="material-icons">clear</i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonLink url icon localizationKey id="" color="background-blue" classes="" isDataUrl=false noUrl=false disabled=false target="">
    <a <#if target?has_content>target="${target}"</#if> <#if !isDataUrl && !noUrl>href="<@s.url url/>"</#if>
       id="${id}"
       class="waves-effect waves-light btn ${color} ${classes}"
        <#if isDataUrl>data-url="${url}"</#if>
        <#if disabled>disabled</#if>>
        <i class="material-icons left <#if !localizationKey?has_content>no-margin</#if>">${icon}</i><#if localizationKey?has_content>${locale.getString(localizationKey)}</#if>
    </a>
</#macro>

<#macro buttonSubmit name icon localizationKey id="" color="background-blue" classes="" disabled=false formaction="" value="" onclick="" form="">
    <button <#if id?has_content>id="${id}"</#if> class="btn waves-effect waves-light ${color} ${classes}" type="submit" name="${name}" <#if disabled>disabled</#if> <#if formaction?has_content>formaction="<@s.url formaction/>"</#if> <#if value?has_content>value="${value}"</#if> <#if onclick?has_content>onclick="${onclick}" </#if> <#if form?has_content>form="${form}" </#if>>
        <i class="material-icons left <#if !localizationKey?has_content>no-margin</#if>">${icon}</i><#if localizationKey?has_content>${locale.getString(localizationKey)}</#if>
    </button>
</#macro>

<#macro buttonFlat url icon localizationKey id="" classes="" isDataUrl=false noUrl=false iconClasses='' target='' datasetIndex=''>
    <a <#if target?has_content>target="${target}"</#if> <#if !isDataUrl && !noUrl>href="<@s.url url/>"</#if>
       id="${id}"
       <#if datasetIndex?has_content>data-index="${datasetIndex}"</#if>
       class="waves-effect waves-light btn-flat ${classes}"
            <#if isDataUrl>data-url="${url}"</#if>>
        <i class="material-icons left <#if !localizationKey?has_content>no-margin</#if> ${iconClasses}">${icon}</i><#if localizationKey?has_content><span>${locale.getString(localizationKey)}</span></#if>
    </a>
</#macro>

<#macro entityIcon entity classes="" fallbackName="">
    <#if entity.getIconReference()??>
        <#if entity.getIconReference().isImageIcon()>
            <img src="<@s.url "/media/getImageByIconID/" + entity.getIconReference().getID()/>" class="${classes}"/>
        <#elseif entity.getIconReference().isBuiltinIcon()>
            <i class="${entity.getIconReference().getBuiltinIdentifier()} ${classes}"></i>
        <#else>
            ${fallbackName?capitalize[0]}
        </#if>
    <#else>
        ${fallbackName?capitalize[0]}
    </#if>
</#macro>