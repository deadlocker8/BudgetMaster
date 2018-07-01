<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "payments"/>
        <@header.style "datepicker"/>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../navbar.ftl" as navbar>
        <@navbar.navbar "payments"/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><#if payment.getID()??>${locale.getString("title.payment.edit")}<#else>${locale.getString("title.payment.new")}</#if></div>
                    </div>
                </div>
                <div class="container">
                    <#import "../validation.ftl" as validation>
                    <form name="NewPayment" action="/payments/newPayment" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="<#if payment.getID()??>${payment.getID()}</#if>">
                        <input type="hidden" name="isRepeating" value="${payment.isRepeating()?c}">

                        <#-- isPayment switch -->
                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2 center-align">
                                <div class="switch">
                                    <label>
                                        ${locale.getString("title.income")}
                                        <input type="checkbox" name="isPayment" <#if payment.getAmount()?? && payment.getAmount() < 0>checked</#if>>
                                        <span class="lever"></span>
                                        ${locale.getString("title.payment")}
                                    </label>
                                </div>
                            </div>
                        </div>

                        <#-- name -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="payment-name" type="text" name="name" <@validation.validation "name"/> value="<#if payment.getName()??>${payment.getName()}</#if>">
                                <label for="payment-name">${locale.getString("payment.new.label.name")}</label>
                            </div>
                        </div>

                        <#-- amount -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="payment-amount" type="text" <@validation.validation "amount"/> value="<#if payment.getAmount()??>${helpers.getAmountString(payment.getAmount())}</#if>">
                                <label for="payment-amount">${locale.getString("payment.new.label.amount")}</label>
                            </div>
                            <input type="hidden" id="hidden-payment-amount" name="amount" value="<#if payment.getAmount()??>${payment.getAmount()}</#if>">
                        </div>

                        <#-- category -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <select id="payment-category" name="category" <@validation.validation "category"/>>
                                    <#list categories as category>
                                        <#if payment.getCategory()??>
                                            <#if payment.getCategory().getID() == category.getID()>
                                                <option selected value="${category.getID()}">${category.getName()}</option>
                                            <#elseif category.getType() != "REST">
                                                <option value="${category.getID()}">${category.getName()}</option>
                                            </#if>
                                        <#elseif category.getType() == "NONE">
                                            <option selected value="${category.getID()}">${category.getName()}</option>
                                        <#else>
                                            <option value="${category.getID()}">${category.getName()}</option>
                                        </#if>
                                    </#list>
                                </select>
                                <label for="payment-category">${locale.getString("payment.new.label.category")}</label>
                            </div>
                        </div>

                        <#-- date -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="payment-datepicker" type="text" class="datepicker" name="date" value="<#if payment.getDate()??>${helpers.getLongDateString(payment.getDate())}<#else>${helpers.getLongDateString(currentDate)}</#if>">
                                <label for="payment-datepicker">${locale.getString("payment.new.label.date")}</label>
                            </div>
                        </div>

                        <#-- description -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <textarea id="payment-description" class="materialize-textarea" name="description" <@validation.validation "description"/>><#if payment.getDescription()??>${payment.getDescription()}</#if></textarea>
                                <label for="payment-description">${locale.getString("payment.new.label.description")}</label>
                            </div>
                        </div>

                        <#-- tags -->
                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2">
                                <label class="chips-label" for="payment-chips">${locale.getString("payment.new.label.tags")}</label>
                                <div id="payment-chips" class="chips chips-placeholder chips-autocomplete"></div>
                            </div>
                            <div id="hidden-payment-tags"></div>
                            <script>
                                var initialTags = [
                                    <#if payment.getTags()??>
                                        <#list payment.getTags() as tag>
                                            {tag: '${tag.getName()}'},
                                        </#list>
                                    </#if>
                                ];
                            </script>
                        </div>

                        <#-- account -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <select id="payment-account" name="account" <@validation.validation "account"/>>
                                    <#list accounts as account>
                                        <#if payment.getAccount()?? && payment.getAccount() == account>
                                            <option selected value="${account.getID()}">${account.getName()}</option>
                                        <#else>
                                            <#if account == helpers.getCurrentAccount()>
                                                <option selected value="${account.getID()}">${account.getName()}</option>
                                            <#else>
                                                <option value="${account.getID()}">${account.getName()}</option>
                                            </#if>
                                        </#if>
                                    </#list>
                                </select>
                                <label for="payment-account">${locale.getString("payment.new.label.account")}</label>
                            </div>
                        </div>

                        <#-- repeating options -->
                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2">
                                <div class="switch">
                                    <label>
                                        <input type="checkbox" id="enableRepeating" name="enableRepeating" <#if payment.getRepeatingOption()??>checked</#if>>
                                        <span class="lever"></span>
                                        ${locale.getString("payment.new.label.repeating")}
                                    </label>
                                </div>
                            </div>
                        </div>

                        <#-- repeating modifier -->
                        <div class="row" id="payment-repeating-modifier-row">
                            <div class="input-field col s6 m6 l4 offset-l2">
                                <input id="payment-repeating-modifier" type="text" <@validation.validation "repeatingModifierNumber"/> value="<#if payment.getRepeatingOption()??>${payment.getRepeatingOption().getModifier().getQuantity()}</#if>">
                                <label for="payment-repeating-modifier">${locale.getString("payment.new.label.repeating.all")}</label>
                            </div>
                            <input type="hidden" id="hidden-payment-repeating-modifier" name="repeatingModifierNumber" value="<#if payment.getRepeatingOption()??>${payment.getRepeatingOption().getModifier().getQuantity()}</#if>">

                            <div class="input-field col s6 m6 l4">
                                <select id="payment-repeating-modifier-type" name="repeatingModifierType">
                                    <#list helpers.getRepeatingModifierTypes() as modifierType>
                                        <#assign modifierName=locale.getString(modifierType.getLocalizationKey())>
                                        <#if payment.getRepeatingOption()??>
                                            <#if payment.getRepeatingOption().getModifier().getLocalizationKey() == modifierName>
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
                        <div class="row" id="payment-repeating-end">
                            <div class="col s12 m12 l8 offset-l2">
                                <div class="row">
                                    <div class="col s12 left-align">
                                        ${locale.getString("repeating.end")}
                                    </div>
                                </div>
                                <@repeatingEndNever (payment.getRepeatingOption()?? && payment.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.never") || !payment.getRepeatingOption()??/>
                                <@repeatingEndAfterXTimes payment.getRepeatingOption()?? && payment.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.afterXTimes"/>
                                <@repeatingEndDate payment.getRepeatingOption()?? && payment.getRepeatingOption().getEndOption().getLocalizationKey() == "repeating.end.key.date"/>
                                <input type="hidden" id="hidden-payment-repeating-end-value" name="repeatingEndValue" value="">
                            </div>
                        </div>

                        <#macro repeatingEndNever checked>
                            <div class="row valign-wrapper">
                                <div class="col s1">
                                    <input class="with-gap" name="repeatingEndType" type="radio" id="repeating-end-never" value="${locale.getString("repeating.end.key.never")}" <#if checked>checked</#if>/>
                                    <label for="repeating-end-never"></label>
                                </div>
                                <div class="col s11">
                                    ${locale.getString("repeating.end.never")}
                                </div>
                            </div>
                        </#macro>

                        <#macro repeatingEndAfterXTimes checked>
                            <div class="row valign-wrapper">
                                <div class="col s1">
                                    <input class="with-gap" name="repeatingEndType" type="radio" id="repeating-end-after-x-times" value="${locale.getString("repeating.end.key.afterXTimes")}" <#if checked>checked</#if>/>
                                    <label for="repeating-end-after-x-times"></label>
                                </div>
                                <div class="col s11">
                                    <table class="table-repeating-end">
                                        <tr>
                                            <td class="cell">${locale.getString("repeating.end.afterXTimes.A")}</td>
                                            <td class="cell input-cell">
                                                <div class="input-field no-margin">
                                                    <input class="no-margin" id="payment-repeating-end-after-x-times-input" type="text" value="<#if checked>${payment.getRepeatingOption().getEndOption().getValue()}</#if>">
                                                    <label for="payment-repeating-end-after-x-times-input"></label>
                                                </div>
                                            </td>
                                            <td class="cell stretched-cell">${locale.getString("repeating.end.afterXTimes.B")}</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </#macro>

                        <#macro repeatingEndDate checked>
                            <div class="row valign-wrapper">
                                <div class="col s1">
                                    <input class="with-gap" name="repeatingEndType" type="radio" id="repeating-end-date" value="${locale.getString("repeating.end.key.date")}" <#if checked>checked</#if>/>
                                    <label for="repeating-end-date"></label>
                                </div>
                                <div class="col s11">
                                    <table class="table-repeating-end">
                                        <tr>
                                            <td class="cell">${locale.getString("repeating.end.date")}</td>
                                            <td class="cell input-cell">
                                                <div class="input-field no-margin">
                                                    <input class="datepicker no-margin" id="payment-repeating-end-date-input" type="text" value="<#if checked>${helpers.getLongDateString(payment.getRepeatingOption().getEndOption().getValue())}<#else>${helpers.getLongDateString(currentDate)}</#if>">
                                                    <label for="payment-repeating-end-date-input"></label>
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
                            <div class="col m6 l4 offset-l2 right-align">
                                <a href="/payments" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                            </div>

                            <div class="col m6 l4 left-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">save</i>${locale.getString("save")}
                                </button>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <a href="/categories" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
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
        <#import "../datePicker.ftl" as datePicker>
        <@datePicker.datePickerLocalization/>
        <script>
            amountValidationMessage = "${locale.getString("warning.payment.amount")}";
            numberValidationMessage = "${locale.getString("warning.payment.number")}";
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

        <!-- Scripts-->
        <#import "../scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="/js/spectrum.js"></script>
        <script src="/js/payments.js"></script>
    </body>
</html>