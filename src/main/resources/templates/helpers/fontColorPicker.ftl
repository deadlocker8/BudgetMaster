<#import "../helpers/header.ftl" as header>

<#macro fontColorPicker item>
    <input type="hidden" name="fontColor" id="fontColor" value="${item.getFontColor(settings.isUseDarkTheme())}">

    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="material-icons prefix">palette</i>
            <label class="input-label" for="fontColorPickerContainer">${locale.getString("account.new.label.icon.fontcolor")}</label>

            <div class="center-align">
                <@header.buttonLink url='' icon='auto_fix_high' id='buttonFontColorAuto' localizationKey='account.new.icon.fontcolor.clear' noUrl=true/>

                <div id="fontColorPickerContainer" class="valign-wrapper">
                    <div id="fontColorPicker" style="background-color: ${item.getFontColor(settings.isUseDarkTheme())}"></div>
                </div>
            </div>
        </div>
    </div>
</#macro>