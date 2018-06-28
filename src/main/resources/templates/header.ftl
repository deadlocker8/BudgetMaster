<#macro style name>
        <#if helpers.getSettings().isUseDarkTheme()>
                <link type="text/css" rel="stylesheet" href=${"/css/dark/" + name + ".css"}/>
        <#else>
                <link type="text/css" rel="stylesheet" href=${"/css/" + name + ".css"}/>
        </#if>
</#macro>

<#macro header title>
        <title>${title}</title>
        <meta charset="UTF-8"/>
        <link rel="stylesheet" href="/fontawesome-5.0.10/css/fontawesome-all.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="/materialize-0.100.2/css/materialize.min.css">
        <@style "style"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</#macro>