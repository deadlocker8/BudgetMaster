<#import "/spring.ftl" as s>
<#import "../helpers/validation.ftl" as validation>
<#import "../helpers/header.ftl" as header>

<#macro isExpenditureSwitch transaction>
    <#assign isExpenditure = true/>
    <#if transaction.isExpenditure()??>
        <#assign isExpenditure=transaction.isExpenditure()/>
    </#if>

    <#if isExpenditure>
        <#assign colorButtonIncome = "background-grey text-isPayment">
        <#assign colorButtonExpenditure = "background-red">
    <#else>
        <#assign colorButtonIncome = "background-green">
        <#assign colorButtonExpenditure = "background-grey text-isPayment">
    </#if>

    <input type="hidden" name="isExpenditure" id="input-isPayment" value="${isExpenditure?c}">

    <div class="row">
        <div class="col s12 center-align">
            <div class="row hide-on-small-only">
                <div class="col s6 right-align">
                    <@buttonIncome colorButtonIncome/>
                </div>
                <div class="col s6 left-align">
                    <@buttonExpenditure colorButtonExpenditure/>
                </div>
            </div>

            <div class="hide-on-med-and-up">
                <div class="row center-align">
                    <div class="col s12">
                        <@buttonIncome colorButtonIncome/>
                    </div>
                </div>
                <div class="row center-align">
                    <div class="col s12">
                        <@buttonExpenditure colorButtonExpenditure/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonIncome color>
    <@header.buttonLink url='' icon='file_download' localizationKey='title.income' color=color classes='buttonIncome' noUrl=true/>
</#macro>

<#macro buttonExpenditure color>
    <@header.buttonLink url='' icon='file_upload' localizationKey='title.expenditure' color=color classes='buttonExpenditure' noUrl=true/>
</#macro>

<#macro transactionName transaction suggestionsJSON>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="material-icons prefix">edit</i>
            <input autocomplete="off" id="transaction-name" type="text" name="name" <@validation.validation "name" "autocomplete"/> value="<#if transaction.getName()??>${transaction.getName()}</#if>">
            <label class="input-label" for="transaction-name">${locale.getString("transaction.new.label.name")}</label>
        </div>
    </div>

    <script>
        transactionNameSuggestions = {};
        var nameSuggestions = ${suggestionsJSON};
        nameSuggestions.forEach(function(name){
            transactionNameSuggestions[name] = null;
        });
    </script>
</#macro>

<#macro transactionAmount transaction>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="material-icons prefix">euro</i>
            <input id="transaction-amount" type="text" <@validation.validation "amount"/> value="<#if transaction.getAmount()??>${currencyService.getAmountString(transaction.getAmount())}</#if>">
            <label class="input-label" for="transaction-amount">${locale.getString("transaction.new.label.amount")}</label>
        </div>
        <input type="hidden" id="hidden-transaction-amount" name="amount" value="<#if transaction.getAmount()??>${transaction.getAmount()}</#if>">
    </div>

    <script>
        amountValidationMessage = "${locale.getString("warning.transaction.amount")}";
        numberValidationMessage = "${locale.getString("warning.empty.number")}";
        dateValidationMessage = "${locale.getString("warning.transaction.date")}";
    </script>
</#macro>

<#macro transactionStartDate transaction currentDate>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <#if transaction.getDate?? && transaction.getDate()??>
                <#assign startDate = dateService.getLongDateString(transaction.getDate())/>
            <#else>
                <#assign startDate = dateService.getLongDateString(currentDate)/>
            </#if>

            <i class="material-icons prefix">event</i>
            <input id="transaction-datepicker" type="text" class="datepicker<#if helpers.isUseSimpleDatepickerForTransactions()>-simple</#if>" name="date" value="${startDate}">
            <label class="input-label" for="transaction-datepicker">${locale.getString("transaction.new.label.date")}</label>
        </div>
    </div>

     <script>
        startDate = "${startDate}".split(".");
        startDate = new Date(startDate[2], startDate[1]-1, startDate[0]);
    </script>
</#macro>

<#macro transactionDescription transaction>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2">
            <i class="material-icons prefix">article</i>
            <textarea id="transaction-description" class="materialize-textarea" name="description" data-length="250" <@validation.validation "description"/>><#if transaction.getDescription()??>${transaction.getDescription()}</#if></textarea>
            <label class="input-label" for="transaction-description">${locale.getString("transaction.new.label.description")}</label>
        </div>
    </div>
</#macro>

<#macro transactionTags transaction>
    <div class="row">
        <div class="col s12 m12 l8 offset-l2">
            <div class="transaction-tags">
                <i class="material-icons prefix">local_offer</i>
                <div class="transaction-tags-input">
                    <label class="input-label" class="chips-label" for="transaction-chips">${locale.getString("transaction.new.label.tags")}</label>
                    <div id="transaction-chips" class="chips chips-placeholder chips-autocomplete"></div>
                </div>
            </div>
        </div>
        <div id="hidden-transaction-tags"></div>
        <script>
            var initialTags = [
                <#if transaction.getTags()??>
                <#list transaction.getTags() as tag>
                {tag: '${tag.getName()?replace("'", "\\'")}'},
                </#list>
                </#if>
            ];
        </script>
    </div>

    <!-- Tag autocomplete -->
    <script>
        tagsPlaceholder = "${locale.getString("tagfield.placeholder")}";
        tagAutoComplete = {
            <#list helpers.getAllTags() as tag>
            '${tag.getName()?replace("'", "\\'")}': null,
            </#list>
        }
    </script>
</#macro>

<#macro transactionRepeating transaction currentDate>
    <div class="row <#if transaction.isRepeating()>hidden</#if>">
        <div class="col s12 center-align">
            <@header.buttonLink url='' icon='repeat' localizationKey='repeating.button.add' id='button-transaction-add-repeating-option' color='background-blue-baby' noUrl=true classes="text-black"/>
        </div>
    </div>

    <div class="<#if !transaction.isRepeating()>hidden</#if>" id="transaction-repeating-option">
        <input type="hidden" name="isRepeating" value="<#if transaction.getRepeatingOption()??>1}</#if>">

        <div class="row">
            <div class="col s12 m12 l8 offset-l2">
                <i class="material-icons icon-repeating">repeat</i>
                ${locale.getString("transaction.new.label.repeating")}
            </div>
        </div>

        <@repeatingModifier transaction/>

        <@repeatingEndOption transaction currentDate/>
    </div>

    <div class="row">
        <div class="col s12 center-align">
            <@header.buttonLink url='' icon='delete' localizationKey='repeating.button.remove' id='button-transaction-remove-repeating-option' color='background-blue-baby' noUrl=true classes="text-black hidden"/>
        </div>
    </div>
</#macro>

<#macro repeatingModifier transaction>
    <div class="row" id="transaction-repeating-modifier-row">
        <div class="input-field col s6 m6 l4 offset-l2">
            <input id="transaction-repeating-modifier" type="text" <@validation.validation "repeatingModifierNumber"/> value="<#if transaction.getRepeatingOption()??>${transaction.getRepeatingOption().getModifier().getQuantity()}</#if>">
            <label for="transaction-repeating-modifier">${locale.getString("transaction.new.label.repeating.all")}</label>
        </div>
        <input type="hidden" id="hidden-transaction-repeating-modifier" name="repeatingModifierNumber" value="<#if transaction.getRepeatingOption()??>${transaction.getRepeatingOption().getModifier().getQuantity()}</#if>">

        <div class="input-field col s6 m6 l4">

            <#if transaction.getRepeatingOption()??>
                <#assign selectedOption=locale.getString(transaction.getRepeatingOption().getModifier().getLocalizationKey())>
            <#else>
                <#assign selectedOption=locale.getString("repeating.modifier.months")>
            </#if>

            <select id="transaction-repeating-modifier-type" name="repeatingModifierType">
                <#list helpers.getRepeatingModifierTypes() as modifierType>
                    <#assign modifierName=locale.getString(modifierType.getLocalizationKey())>
                    <#if selectedOption == modifierName>
                        <option selected value="${modifierName}">${modifierName}</option>
                    <#else>
                        <option value="${modifierName}">${modifierName}</option>
                    </#if>
                </#list>
            </select>
        </div>
    </div>
</#macro>

<#macro repeatingEndOption transaction currentDate>
    <div class="row" id="transaction-repeating-end">
        <div class="col s12 m12 l8 offset-l2">
            <div class="row">
                <div class="col s12 left-align">
                    ${locale.getString("repeating.end")}
                </div>
            </div>
            <@repeatingEndNever (transaction.getRepeatingOption()?? && transaction.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.never") || !transaction.getRepeatingOption()??/>
            <@repeatingEndAfterXTimes transaction.getRepeatingOption()?? && transaction.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.afterXTimes"/>
            <@repeatingEndDate transaction.getRepeatingOption()?? && transaction.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.date" transaction currentDate/>
            <input type="hidden" id="hidden-transaction-repeating-end-value" name="repeatingEndValue" value="">
        </div>
    </div>
</#macro>

<#macro repeatingEndNever checked>
    <div class="row valign-wrapper">
        <div class="col s1">
            <label>
                <input class="with-gap" name="repeatingEndType" type="radio" id="repeating-end-never" value="${locale.getString("repeating.end.key.never")}" <#if checked>checked</#if>/>
                <span for="repeating-end-never"></span>
            </label>
        </div>
        <div class="col s11">
            ${locale.getString("repeating.end.never")}
        </div>
    </div>
</#macro>

<#macro repeatingEndAfterXTimes checked>
    <div class="row valign-wrapper">
        <div class="col s1">
            <label>
                <input class="with-gap" name="repeatingEndType" type="radio" id="repeating-end-after-x-times" value="${locale.getString("repeating.end.key.afterXTimes")}" <#if checked>checked</#if>/>
                <span for="repeating-end-after-x-times"></span>
            </label>
        </div>
        <div class="col s11">
            <table class="table-repeating-end no-border-table">
                <tr>
                    <td class="cell">${locale.getString("repeating.end.afterXTimes.A")}</td>
                    <td class="cell input-cell">
                        <div class="input-field no-margin">
                            <input class="no-margin input-min-width" id="transaction-repeating-end-after-x-times-input" type="text" value="<#if checked>${transaction.getRepeatingOption().getEndOption().getValue()}</#if>">
                            <label for="transaction-repeating-end-after-x-times-input"></label>
                        </div>
                    </td>
                    <td class="cell stretched-cell">${locale.getString("repeating.end.afterXTimes.B")}</td>
                </tr>
            </table>
        </div>
    </div>
</#macro>

<#macro repeatingEndDate checked transaction currentDate>
    <#if checked>
        <#global endDate = dateService.getLongDateString(transaction.getRepeatingOption().getEndOption().getValue())/>
    <#else>
        <#global endDate = dateService.getLongDateString(currentDate)/>
    </#if>

    <script>
        endDate = "${endDate}".split(".");
        endDate = new Date(endDate[2], endDate[1]-1, endDate[0]);
    </script>

    <div class="row valign-wrapper">
        <div class="col s1">
            <label>
                <input class="with-gap" name="repeatingEndType" type="radio" id="repeating-end-date" value="${locale.getString("repeating.end.key.date")}" <#if checked>checked</#if>/>
                <span for="repeating-end-date"></span>
            </label>
        </div>
        <div class="col s11">
            <table class="table-repeating-end no-border-table">
                <tr>
                    <td class="cell">${locale.getString("repeating.end.date")}</td>
                    <td class="cell input-cell">
                        <div class="input-field no-margin">
                            <input class="datepicker<#if helpers.isUseSimpleDatepickerForTransactions()>-simple</#if> no-margin input-min-width" id="transaction-repeating-end-date-input" type="text" value="${endDate}">
                            <label for="transaction-repeating-end-date-input"></label>
                        </div>
                    </td>
                    <td class="cell stretched-cell"></td>
                </tr>
            </table>
        </div>
    </div>
</#macro>

<#macro buttons cancelURL includeContinueButton>
    <div id="keywordCheckUrl" class="hidden" data-url="<@s.url '/keywords/keywordCheck'/>"></div>

    <br>
    <hr>
    <br>

    <div class="row hide-on-small-only">
        <div class="col s12 center-align">
            <@buttonCancel cancelURL/>&nbsp;
            <#if includeContinueButton>
                <@buttonSaveAndContinue/>&nbsp;
            </#if>
            <@buttonSave/>
        </div>
    </div>

    <div class="hide-on-med-and-up">
        <div class="row center-align">
            <div class="col s12">
                <@buttonCancel cancelURL/>
            </div>
        </div>
        <#if includeContinueButton>
            <div class="row center-align">
                <div class="col s12">
                    <@buttonSaveAndContinue/>
                </div>
            </div>
        </#if>
        <div class="row center-align">
            <div class="col s12">
                <@buttonSave/>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonCancel cancelURL>
    <@header.buttonLink url=cancelURL icon='clear' localizationKey='cancel' id='button-cancel-save-transaction' color='red'/>
</#macro>

<#macro buttonSave>
    <@header.buttonSubmit name='action' icon='save' localizationKey='save' id='button-save-transaction' color='green'/>
</#macro>

<#macro buttonSaveAndContinue>
    <@header.buttonSubmit name='action' icon='save' localizationKey='saveAndContinue' id='button-save-transaction-and-continue' value='continue'/>
</#macro>

<#macro buttonTransactionActions canChangeType canCreateTemplate changeTypeInProgress>
    <#if (canChangeType || canCreateTemplate) && !changeTypeInProgress>
        <div class="fixed-action-btn" id="transaction-actions-button">
            <a class="btn-floating btn-large waves-effect waves-light background-blue">
                <i class="material-icons left">settings</i>${locale.getString("save")}
            </a>
            <ul>
                <#if canChangeType>
                    <li>
                        <a class="btn-floating btn transaction-action mobile-fab-tip no-wrap" data-action-type="changeType" data-url="<@s.url '/transactions/${transaction.getID()?c}/changeTypeModal'/>">${locale.getString("transaction.change.type")}</a>
                        <a class="btn-floating btn transaction-action background-blue-baby" data-action-type="changeType" data-url="<@s.url '/transactions/${transaction.getID()?c}/changeTypeModal'/>"><i class="material-icons">shuffle</i></a>
                    </li>
                </#if>
                <#if canCreateTemplate>
                    <li>
                        <a class="btn-floating btn transaction-action mobile-fab-tip no-wrap" data-action-type="saveAsTemplate" data-url="<@s.url '/templates/fromTransactionModal'/>">${locale.getString("save.as.template")}</a>
                        <a class="btn-floating btn transaction-action background-orange-dark" data-action-type="saveAsTemplate" data-url="<@s.url '/templates/fromTransactionModal'/>"><i class="material-icons">file_copy</i></a>
                    </li>
                </#if>
            </ul>
        </div>
    </#if>
</#macro>