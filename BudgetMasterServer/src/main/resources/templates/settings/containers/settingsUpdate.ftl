<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>
<#import "../settingsMacros.ftl" as settingsMacros>

<#macro updateSettingsContainer importScripts settings>
    <@settingsContainerMacros.settingsContainer 'UpdateSettingsContainer' 'updateSettingsContainer' importScripts>
        <div class="row">
            <div class="col s12 m12 l8 offset-l2 center-align">
                <div class="table-container">
                    <div class="table-cell">
                        <div class="right-align" style="margin-bottom: 1em;">${locale.getString("settings.updates.current.version")}</div>
                        <div class="right-align">${locale.getString("settings.updates.latest.version")}</div>
                    </div>

                    <div class="table-cell table-cell-spacer"></div>

                    <div class="table-cell">
                        <div class="left-align" style="margin-bottom: 1em; margin-right: 5em">
                            <div class="banner background-grey <#if settings.isUseDarkTheme()>text-black<#else>text-white</#if>">
                                v${build.getVersionName()}
                            </div>
                        </div>
                        <div class="left-align">
                            <#if updateService.getAvailableVersionString() == "-">
                                <#if settings.isUseDarkTheme()>
                                    <#assign bannerClasses="background-grey text-black">
                                <#else>
                                    <#assign bannerClasses="background-grey text-white">
                                </#if>
                            <#else>
                                <#if updateService.isUpdateAvailable()>
                                    <#assign bannerClasses="background-orange text-black">
                                <#else>
                                    <#assign bannerClasses="background-green text-white">
                                </#if>
                            </#if>

                            <div class="banner ${bannerClasses}">
                                ${updateService.getAvailableVersionString()}
                            </div>
                        </div>
                    </div>

                    <div class="table-cell table-cell-valign">
                        <@header.buttonLink url='/settings/updateSearch' icon='refresh' localizationKey='settings.updates.search' isDataUrl=true id='button-update-search'/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <div class="table-container">
                    <div class="table-cell">
                        <div class="switch-cell-margin">${locale.getString("settings.updates.automatic")}</div>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <@settingsMacros.switch "updates.automatic" "autoUpdateCheckEnabled" settings.isAutoUpdateCheckEnabled()/>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <div class="switch-cell-margin">
                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.updates.automatic.description")}"><i class="material-icons">help_outline</i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green' formaction='/settings/save/update'/>
            </div>
        </div>

        <script>
            $('#button-update-search').click(function()
            {
                $.ajax({
                    type: 'GET',
                    url: $(this).attr('data-url'),
                    data: {},
                    success: function(response)
                    {
                        M.Toast.dismissAll();

                        let parsedData = JSON.parse(response);
                        M.toast({
                            html: parsedData['localizedMessage'],
                            classes: parsedData['classes']
                        });
                    },
                    error: function(response)
                    {
                        M.toast({
                            html: "Error searching for updates",
                            classes: 'red'
                        });
                        console.error(response);
                    }
                });
            });

            $('input[name="autoUpdateCheckEnabled"]').change(function()
            {
                toggleSettingsContainerHeader('updateSettingsContainerHeader', false);
            });
        </script>
    </@settingsContainerMacros.settingsContainer>
</#macro>

<@updateSettingsContainer importScripts=true settings=settings/>
