<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>
<#import "../settingsMacros.ftl" as settingsMacros>

<#macro personalizationSettingsContainer importScripts settings showReloadWarning>
    <@settingsContainerMacros.settingsContainer 'PersonalizationSettingsContainer' 'personalizationSettingsContainer' importScripts '/settings/save/personalization'>
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

        <!-- theme -->
        <div class="row center-align">
            <div class="col col s6 m6 l4 offset-l2">
                <div>${locale.getString("settings.darkTheme.deactivated")}</div>
                <img class="responsive-img settings-preview-image <#if !settings.isUseDarkTheme()>settings-preview-image-selected</#if>" src="<@s.url '/images/settings/themeLight.png'/>" data-use-dark-theme="false"/>
            </div>

            <div class="col col s6 m6 l4">
                <div>${locale.getString("settings.darkTheme.activated")}</div>
                <img class="responsive-img settings-preview-image <#if settings.isUseDarkTheme()>settings-preview-image-selected</#if>" src="<@s.url '/images/settings/themeDark.png'/>" data-use-dark-theme="true"/>
            </div>

            <input type="hidden" id="useDarkTheme" name="useDarkTheme" value="${settings.isUseDarkTheme()?c}"/>
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
                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green'/>
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

            $('.settings-preview-image').click(function()
            {
                for(let item of document.getElementsByClassName('settings-preview-image'))
                {
                    item.classList.toggle('settings-preview-image-selected', false);
                }

                this.classList.toggle('settings-preview-image-selected', true);

                document.getElementById('useDarkTheme').value = this.dataset.useDarkTheme;
                toggleSettingsContainerHeader('personalizationSettingsContainerHeader', false);
            });

            // show unsaved changes warning
            $('#settings-language').change(function()
            {
                toggleSettingsContainerHeader('personalizationSettingsContainerHeader', false);
            });

            $('#settings-currency').on('change keydown paste input', function()
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
