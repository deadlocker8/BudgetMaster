<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>
<#import "../settingsMacros.ftl" as settingsMacros>

<#macro personalizationSettingsContainer importScripts settings showReloadWarning>
    <@settingsContainerMacros.settingsContainer 'PersonalizationSettingsContainer' 'personalizationSettingsContainer' importScripts>
        <#-- language -->
        <div class="row">
            <div class="input-field col s12 m12 l8 offset-l2">
                <i class="material-icons prefix">translate</i>
                <select id="settings-language" name="language" <@validation.validation "language"/>>
                    <#list helpers.getAvailableLanguages() as language>
                        <#if settings.getLanguage() == language>
                            <option selected value="${language.getName()}">${language.getName()}</option>
                        <#else>
                            <option value="${language.getName()}">${language.getName()}</option>
                        </#if>
                    </#list>
                </select>
                <label for="settings-language">${locale.getString("settings.language")}</label>
            </div>
        </div>

        <#-- currency -->
        <div class="row">
            <div class="input-field col s12 m12 l8 offset-l2">
                <i class="material-icons prefix">euro</i>
                <input id="settings-currency" type="text" name="currency" <@validation.validation "currency"/> value="<#if settings.getCurrency()??>${settings.getCurrency()}</#if>">
                <label for="settings-currency">${locale.getString("settings.currency")}</label>
            </div>
        </div>

        <#-- dark theme and category style -->
        <@settingsMacros.switches settings/>

        <#-- search items per page -->
        <div class="row">
            <div class="input-field col s12 m12 l8 offset-l2">
                <i class="material-icons prefix">search</i>
                <select id="settings-search-items-per-page" name="searchItemsPerPage" <@validation.validation "searchItemsPerPage"/>>
                    <#list searchResultsPerPageOptions as number>
                        <#if settings.getSearchItemsPerPage() == number>
                            <option selected value="${number}">${number}</option>
                        <#else>
                            <option value="${number}">${number}</option>
                        </#if>
                    </#list>
                </select>
                <label for="settings-search-items-per-page">${locale.getString("settings.search.itemsPerPage")}</label>
            </div>
        </div>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green' formaction='/settings/save/personalization'/>
            </div>
        </div>

        <#if showReloadWarning>
            <div class="row notification-row">
                <div class="col s12 center-align">
                    <div class="notification-wrapper">
                        <div class="notification background-yellow text-black">
                            <i class="fas fa-exclamation-triangle notification-item"></i>
                            <span class="notification-item left-align">${locale.getString('settings.personalization.reload.page')}</span>
                        </div>
                    </div>
                </div>
            </div>
        </#if>

        <script>
            $('#settings-language').change(function()
            {
                toggleSettingsContainerHeader('personalizationSettingsContainerHeader', false);
            });

            $('#settings-currency').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('personalizationSettingsContainerHeader', false);
            });

            $('input[name="useDarkTheme"]').change(function()
            {
                toggleSettingsContainerHeader('personalizationSettingsContainerHeader', false);
            });

            $('input[name="showCategoriesAsCircles"]').change(function()
            {
                toggleSettingsContainerHeader('personalizationSettingsContainerHeader', false);
            });

            $('#settings-password-confirmation').on('change keydown paste input', function()
            {
                toggleSettingsContainerHeader('personalizationSettingsContainerHeader', false);
            });

            $('#settings-search-items-per-page').change(function()
            {
                toggleSettingsContainerHeader('personalizationSettingsContainerHeader', false);
            });
        </script>
    </@settingsContainerMacros.settingsContainer>
</#macro>

<@personalizationSettingsContainer importScripts=true settings=settings showReloadWarning=true/>
