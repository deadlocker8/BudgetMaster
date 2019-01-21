<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "transactions"/>
        <@header.style "datepicker"/>
        <@header.style "categories"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "transactions"/>

        <#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><#if transaction.getID()??>${locale.getString("title.transaction.edit")}<#else>${locale.getString("title.transaction.new")}</#if></div>
                    </div>
                </div>
                <div class="container">
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="NewTransaction" action="<@s.url '/transactions/newTransaction'/>" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="<#if transaction.getID()??>${transaction.getID()?c}</#if>">
                        <input type="hidden" name="isRepeating" value="${transaction.isRepeating()?c}">

                        <#-- isPayment switch -->
                        <div class="row">
                            <div class="col s12 center-align">
                                <div class="row hide-on-small-only">
                                    <div class="col s6 right-align">
                                        <#assign isPayment = 1>

                                        <#if transaction.getAmount()?? && (transaction.getAmount() > 0)>
                                            <#assign colorButtonIncome = "budgetmaster-green">
                                            <#assign isPayment = 0>
                                        <#else>
                                            <#assign colorButtonIncome = "budgetmaster-grey budgetmaster-text-isPayment">
                                        </#if>
                                        <a class="waves-effect waves-light btn ${colorButtonIncome}" id="buttonIncome"><i class="material-icons left">file_download</i>${locale.getString("title.income")}</a>
                                    </div>
                                    <div class="col s6 left-align">
                                        <#if transaction.getAmount()?? && (transaction.getAmount() > 0)>
                                            <#assign colorButtonExpenditure = "budgetmaster-grey budgetmaster-text-isPayment">
                                        <#else>
                                            <#assign colorButtonExpenditure = "budgetmaster-red">
                                        </#if>
                                        <a class="waves-effect waves-light btn ${colorButtonExpenditure}" id="buttonExpenditure"><i class="material-icons left">file_upload</i>${locale.getString("title.expenditure")}</a>
                                    </div>
                                    <input type="hidden" name="isPayment" id="input-isPayment" value="${isPayment}">
                                </div>

                                <div class="hide-on-med-and-up">
                                    <div class="row center-align">
                                        <div class="col s12">
                                            <#assign isPayment = 1>

                                            <#if transaction.getAmount()?? && (transaction.getAmount() > 0)>
                                                <#assign colorButtonIncome = "budgetmaster-green">
                                                <#assign isPayment = 0>
                                            <#else>
                                                <#assign colorButtonIncome = "budgetmaster-grey budgetmaster-text-isPayment">
                                            </#if>
                                            <a class="waves-effect waves-light btn ${colorButtonIncome}" id="buttonIncome"><i class="material-icons left">file_download</i>${locale.getString("title.income")}</a>
                                        </div>
                                    </div>
                                    <div class="row center-align">
                                        <div class="col s12">
                                            <#if transaction.getAmount()?? && (transaction.getAmount() > 0)>
                                                <#assign colorButtonExpenditure = "budgetmaster-grey budgetmaster-text-isPayment">
                                            <#else>
                                                <#assign colorButtonExpenditure = "budgetmaster-red">
                                            </#if>
                                            <a class="waves-effect waves-light btn ${colorButtonExpenditure}" id="buttonExpenditure"><i class="material-icons left">file_upload</i>${locale.getString("title.expenditure")}</a>
                                        </div>
                                        <input type="hidden" name="isPayment" id="input-isPayment" value="${isPayment}">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <#-- name -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="transaction-name" type="text" name="name" <@validation.validation "name"/> value="<#if transaction.getName()??>${transaction.getName()}</#if>">
                                <label for="transaction-name">${locale.getString("transaction.new.label.name")}</label>
                            </div>
                        </div>

                        <#-- amount -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="transaction-amount" type="text" <@validation.validation "amount"/> value="<#if transaction.getAmount()??>${helpers.getAmountString(transaction.getAmount())}</#if>">
                                <label for="transaction-amount">${locale.getString("transaction.new.label.amount")}</label>
                            </div>
                            <input type="hidden" id="hidden-transaction-amount" name="amount" value="<#if transaction.getAmount()??>${transaction.getAmount()}</#if>">
                        </div>

                        <#-- category -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2" id="categoryWrapper">
                                <select id="transaction-category" name="category" <@validation.validation "category"/>>
                                    <#list categories as category>
                                        <#assign categoryInfos=categoriesFunctions.getCategoryName(category) + "@@@" + category.getColor() + "@@@" + category.getAppropriateTextColor() + "@@@" + category.getID()?c>

                                        <#if transaction.getCategory()??>
                                            <#if transaction.getCategory().getID()?c == category.getID()?c>
                                                <option selected value="${category.getID()?c}">${categoryInfos}</option>
                                            <#elseif category.getType() != "REST">
                                                <option value="${category.getID()?c}">${categoryInfos}</option>
                                            </#if>
                                        <#elseif category.getType() == "NONE">
                                            <option selected value="${category.getID()?c}">${categoryInfos}</option>
                                        <#elseif category.getType() != "REST">
                                            <option value="${category.getID()?c}">${categoryInfos}</option>
                                        </#if>
                                    </#list>
                                </select>
                                <label for="transaction-category">${locale.getString("transaction.new.label.category")}</label>
                            </div>
                        </div>

                        <#-- date -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <#if transaction.getDate()??>
                                    <#assign startDate = helpers.getLongDateString(transaction.getDate())/>
                                <#else>
                                    <#assign startDate = helpers.getLongDateString(currentDate)/>
                                </#if>

                                <input id="transaction-datepicker" type="text" class="datepicker" name="date" value="${startDate}">
                                <label for="transaction-datepicker">${locale.getString("transaction.new.label.date")}</label>
                            </div>
                        </div>

                        <#-- description -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <textarea id="transaction-description" class="materialize-textarea" name="description" <@validation.validation "description"/>><#if transaction.getDescription()??>${transaction.getDescription()}</#if></textarea>
                                <label for="transaction-description">${locale.getString("transaction.new.label.description")}</label>
                            </div>
                        </div>

                        <#-- tags -->
                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2">
                                <label class="chips-label" for="transaction-chips">${locale.getString("transaction.new.label.tags")}</label>
                                <div id="transaction-chips" class="chips chips-placeholder chips-autocomplete"></div>
                            </div>
                            <div id="hidden-transaction-tags"></div>
                            <script>
                                var initialTags = [
                                    <#if transaction.getTags()??>
                                        <#list transaction.getTags() as tag>
                                            {tag: '${tag.getName()}'},
                                        </#list>
                                    </#if>
                                ];
                            </script>
                        </div>

                        <#-- account -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <select id="transaction-account" name="account" <@validation.validation "account"/>>
                                    <#list accounts as account>
                                        <#if (account.getType().name() == "CUSTOM")>
                                            <#if transaction.getAccount()?? && transaction.getAccount() == account>
                                                <option selected value="${account.getID()?c}">${account.getName()}</option>
                                            <#else>
                                                <#if account == helpers.getCurrentAccount()>
                                                    <option selected value="${account.getID()?c}">${account.getName()}</option>
                                                <#else>
                                                    <option value="${account.getID()?c}">${account.getName()}</option>
                                                </#if>
                                            </#if>
                                        </#if>
                                    </#list>
                                </select>
                                <label for="transaction-account">${locale.getString("transaction.new.label.account")}</label>
                            </div>
                        </div>

                        <#-- repeating options -->
                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2">
                                <div class="switch">
                                    <label>
                                        <input type="checkbox" id="enableRepeating" name="enableRepeating" <#if transaction.getRepeatingOption()??>checked</#if>>
                                        <span class="lever"></span>
                                        ${locale.getString("transaction.new.label.repeating")}
                                    </label>
                                </div>
                            </div>
                        </div>

                        <#-- repeating modifier -->
                        <div class="row" id="transaction-repeating-modifier-row">
                            <div class="input-field col s6 m6 l4 offset-l2">
                                <input id="transaction-repeating-modifier" type="text" <@validation.validation "repeatingModifierNumber"/> value="<#if transaction.getRepeatingOption()??>${transaction.getRepeatingOption().getModifier().getQuantity()}</#if>">
                                <label for="transaction-repeating-modifier">${locale.getString("transaction.new.label.repeating.all")}</label>
                            </div>
                            <input type="hidden" id="hidden-transaction-repeating-modifier" name="repeatingModifierNumber" value="<#if transaction.getRepeatingOption()??>${transaction.getRepeatingOption().getModifier().getQuantity()}</#if>">

                            <div class="input-field col s6 m6 l4">
                                <select id="transaction-repeating-modifier-type" name="repeatingModifierType">
                                    <#list helpers.getRepeatingModifierTypes() as modifierType>
                                        <#assign modifierName=locale.getString(modifierType.getLocalizationKey())>
                                        <#if transaction.getRepeatingOption()??>
                                            ${transaction.getRepeatingOption().getModifier().getLocalizationKey()}
                                            <#if locale.getString(transaction.getRepeatingOption().getModifier().getLocalizationKey()) == modifierName>
                                                <option selected value="${modifierName}">${modifierName}</option>
                                            <#else>
                                                <option value="${modifierName}">${modifierName}</option>
                                            </#if>
                                        <#else>
                                            <option value="${modifierName}">${modifierName}</option>
                                        </#if>
                                    </#list>
                                </select>
                            </div>
                        </div>

                        <#-- repeating end option -->
                        <div class="row" id="transaction-repeating-end">
                            <div class="col s12 m12 l8 offset-l2">
                                <div class="row">
                                    <div class="col s12 left-align">
                                        ${locale.getString("repeating.end")}
                                    </div>
                                </div>
                                <@repeatingEndNever (transaction.getRepeatingOption()?? && transaction.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.never") || !transaction.getRepeatingOption()??/>
                                <@repeatingEndAfterXTimes transaction.getRepeatingOption()?? && transaction.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.afterXTimes"/>
                                <@repeatingEndDate transaction.getRepeatingOption()?? && transaction.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.date"/>
                                <input type="hidden" id="hidden-transaction-repeating-end-value" name="repeatingEndValue" value="">
                            </div>
                        </div>

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

                        <#macro repeatingEndDate checked>
                            <#if checked>
                                <#assign endDate = helpers.getLongDateString(transaction.getRepeatingOption().getEndOption().getValue())/>
                            <#else>
                                <#assign endDate = helpers.getLongDateString(currentDate)/>
                            </#if>

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
                                                    <input class="datepicker no-margin input-min-width" id="transaction-repeating-end-date-input" type="text" value="${endDate}">
                                                    <label for="transaction-repeating-end-date-input"></label>
                                                </div>
                                            </td>
                                            <td class="cell stretched-cell"></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </#macro>

                        <br>

                        <#-- buttons -->
                        <div class="row hide-on-small-only">
                            <div class="col s6 right-align">
                                <a href="<@s.url '/transactions'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                            </div>

                            <div class="col s6 left-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">save</i>${locale.getString("save")}
                                </button>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <a href="<@s.url '/categories'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                                </div>
                            </div>
                            <div class="row center-align">
                                <div class="col s12">
                                    <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave">
                                        <i class="material-icons left">save</i>${locale.getString("save")}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!-- Pass localization to JS -->
        <#import "../helpers/globalDatePicker.ftl" as datePicker>
        <@datePicker.datePickerLocalization/>
        <script>
            startDate = "${startDate}".split(".");
            startDate = new Date(startDate[2], startDate[1]-1, startDate[0]);

            endDate = "${endDate}".split(".");
            endDate = new Date(endDate[2], endDate[1]-1, endDate[0]);

            amountValidationMessage = "${locale.getString("warning.transaction.amount")}";
            numberValidationMessage = "${locale.getString("warning.transaction.number")}";
            tagsPlaceholder = "${locale.getString("tagfield.placeholder")}";
        </script>

        <!-- Tag autocomplete -->
        <script>
            tagAutoComplete = {
                <#list helpers.getAllTags() as tag>
                    '${tag.getName()}': null,
                </#list>
            }
        </script>

        <#-- pass selected account to JS in order to select current value for materialize select -->
        <script>
            <#if transaction.getCategory()??>
                selectedCategory = "${transaction.getCategory().getID()?c}";
            <#else>
                selectedCategory = "${helpers.getIDOfNoCatgeory()?c}";
            </#if>
        </script>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/spectrum.js'/>"></script>
        <script src="<@s.url '/js/transactions.js'/>"></script>
    </body>
</html>