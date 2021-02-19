<#macro style name>
    <#import "/spring.ftl" as s>

    <#assign blacklist = ['categories', 'charts', 'collapsible', 'datepicker', 'globalDatepicker', 'hotkeys', 'login', 'search']>

    <#if helpers.getSettings().isUseDarkTheme() && !blacklist?seq_contains(name)>
        <link type="text/css" rel="stylesheet" href="<@s.url '${"/css/dark/" + name + ".css"}'/>"/>
    <#else>
        <link type="text/css" rel="stylesheet" href="<@s.url '${"/css/" + name + ".css"}'/>"/>
    </#if>
</#macro>

<#macro globals>
    <#global locale = static["de.thecodelabs.utils.util.Localization"]>
    <#global programArgs = static["de.deadlocker8.budgetmaster.ProgramArgs"]>

    <#if helpers.getSettings().isUseDarkTheme()>
        <#global greenTextColor="text-green"/>
        <#global redTextColor="text-light-red"/>
    <#else>
        <#global greenTextColor="text-dark-green"/>
        <#global redTextColor="text-red"/>
    </#if>
</#macro>

<#macro header title>
    <#import "/spring.ftl" as s>
    <title>${title}</title>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="<@s.url '/webjars/font-awesome/5.15.1/css/all.min.css'/>">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="<@s.url "/webjars/materializecss/1.0.0/css/materialize.min.css"/>">
    <link type="text/css" rel="stylesheet" href="<@s.url "/css/colors.css"/>"/>
    <@style "style"/>
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

<#macro modalConfirmDelete title confirmUrl cancelUrlBase itemId confirmButtonText id="modalConfirmDelete" classes="">
    <div id="${id}" class="modal background-color ${classes}">
        <div class="modal-content">
            <h4>${title}</h4>

            <#nested>
        </div>
        <div class="modal-footer background-color">
            <a href="<@s.url confirmUrl/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
            <a href="<@s.url cancelUrlBase + "/" + itemId?c + "/delete" />" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${confirmButtonText}</a>
        </div>
    </div>
</#macro>

<#macro body>
    <body class="background-blue-light" <#if settings.isUseDarkTheme()>data-theme="dark"</#if>>
        <#nested>
    </body>
</#macro>