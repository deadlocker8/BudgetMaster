<#import "/spring.ftl" as s>
<#import "../../helpers/validation.ftl" as validation>
<#import "../../helpers/header.ftl" as header>
<@header.globals/>

<#import "settingsContainer.ftl" as settingsContainerMacros>
<#import "../settingsMacros.ftl" as settingsMacros>

<#macro transactionsSettingsContainer importScripts settings>
    <@settingsContainerMacros.settingsContainer 'TransactionsSettingsContainer' 'transactionsSettingsContainer' importScripts '/settings/save/transactions' 'validateTransactionForm()'>
        <div class="row">
            <div class="col s12">
                <div class="table-container">
                    <div class="table-cell">
                        <div class="switch-cell-margin">${locale.getString("settings.rest")}</div>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <@settingsMacros.switch "rest" "restActivated" settings.isRestActivated()/>
                    </div>
                    <div class="table-cell table-cell-spacer"></div>
                    <div class="table-cell">
                        <div class="switch-cell-margin">
                            <a class="btn btn-flat tooltipped text-default" data-position="bottom" data-tooltip="${locale.getString("settings.rest.description")}"><i class="material-icons">help_outline</i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row no-margin-bottom">
            <div class="col s12 m12 l8 offset-l2">
                <div class="tag-input-container">
                    <i class="material-icons prefix">rule_folder</i>
                    <div class="tag-input">
                        <label class="input-label" class="chips-label" for="settings-keywords">${locale.getString("settings.transactions.keywords")}</label>
                        <div id="settings-keywords" class="chips chips-placeholder"></div>
                    </div>
                </div>
            </div>
            <div id="hidden-transaction-name-keywords"></div>
            <script>
                keywordsPlaceholder = "${locale.getString("settings.transactions.keywords.placeholder")}";
                var initialKeywords = [
                    <#list transactionNameKeywords as keyword>
                    {tag: '${keyword.getValue()?replace("'", "\\'")}'},
                    </#list>
                ];
            </script>
        </div>

        <div class="row">
            <div class="col s10 offset-s1 m8 offset-m2 l6 offset-l3">
                <div id="settings-keywords-description">
                    <i class="material-icons">help_outline</i>
                    <div>
                        ${locale.getString("settings.transactions.keywords.description")}
                    </div>
                </div>
            </div>
        </div>

        <br>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='background-green'/>
            </div>
        </div>

        <script>
            M.Chips.init(document.querySelectorAll('.chips'), {
                placeholder: keywordsPlaceholder,
                secondaryPlaceholder: keywordsPlaceholder,
                data: initialKeywords,
                onChipAdd: onKeywordsChange,
                onChipDelete: onKeywordsChange
            });

            function onKeywordsChange()
            {
                toggleSettingsContainerHeader('transactionsSettingsContainerHeader', false);
            }

            function validateTransactionForm()
            {
                let keywords = M.Chips.getInstance(document.querySelector('.chips')).chipsData;
                let parent = document.getElementById('hidden-transaction-name-keywords');
                for(let i = 0; i < keywords.length; i++)
                {
                    let input = document.createElement('input');
                    input.setAttribute('type', 'hidden');
                    input.setAttribute('name', 'keywords[' + i + '].value');
                    input.setAttribute('value', keywords[i].tag);
                    parent.appendChild(input);
                }

                return false;
            }

            $('input[name="restActivated"]').change(function()
            {
                toggleSettingsContainerHeader('transactionsSettingsContainerHeader', false);
            });
        </script>
    </@settingsContainerMacros.settingsContainer>
</#macro>

<@transactionsSettingsContainer importScripts=true settings=settings/>
