<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.hotkeys')}"/>
    </head>
    <@header.body>
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "hotkeys" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">keyboard</i> ${locale.getString("menu.hotkeys")}</div>
                    </div>
                </div>

                <@header.content>
                    <br>

                    <@listHotKeys locale.getString("hotkeys.general") hotkeysGeneral/>

                    <@listHotKeys locale.getString("hotkeys.global.datepicker") hotkeysGlobalDatePicker/>

                    <@listHotKeys locale.getString("hotkeys.global.account.select") hotkeysAccountSelect/>
                </@header.content>
            </div>
        </main>

        <!--  Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>

<#macro cellKey key>
    <div class="col s4 m3 offset-m2 l2 offset-l3 right-align bold">
        <div class="keyboard-key">${key}</div>
    </div>
</#macro>

<#macro cellKeyWithModifier modifier key>
    <div class="col s4 m3 offset-m2 l2 offset-l3 right-align bold">
        <#if modifier?? && modifier?has_content>
            <div class="keyboard-key modifier-key">${modifier}</div>
            <span class="bold">+</span>
        </#if>
        <div class="keyboard-key">${key}</div>
    </div>
</#macro>

<#macro listHotKeys headline hotKeys>
    <div class="row">
        <div class="col s12 headline center-align">${headline}</div>
    </div>

    <#list hotKeys as hotKey>
        <div class="row">
            <@cellKeyWithModifier hotKey.getModifierLocalized()!'' hotKey.getKeyLocalized()/>
            <div class="col s8 m5 l5">${hotKey.getTextLocalized()}</div>
        </div>
    </#list>
</#macro>