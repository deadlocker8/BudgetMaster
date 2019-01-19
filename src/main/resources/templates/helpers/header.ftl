<#macro style name>
        <#import "/spring.ftl" as s>
        <#if helpers.getSettings().isUseDarkTheme()>
                <link type="text/css" rel="stylesheet" href=<@s.url '${"/css/dark/" + name + ".css"}'/>/>
        <#else>
                <link type="text/css" rel="stylesheet" href=<@s.url '${"/css/" + name + ".css"}'/>/>
        </#if>
</#macro>

<#macro header title>
        <#import "/spring.ftl" as s>
        <title>${title}</title>
        <meta charset="UTF-8"/>
        <link rel="stylesheet" href="<@s.url '/fontawesome-5.0.10/css/fontawesome-all.min.css'/>">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="<@s.url '/materialize-v1.0.0/css/materialize.min.css'/>">
        <@style "style"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
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

<#macro logo id classes>
        <#import "/spring.ftl" as s>
        <#if helpers.getSettings().isUseDarkTheme()>
                <img class="${classes}" id="${id}" src="<@s.url '/images/Logo_with_white_text_medium_res.png'/>">
        <#else>
                <img class="${classes}" id="${id}" src="<@s.url '/images/Logo_with_text_medium_res.png'/>">
        </#if>
</#macro>