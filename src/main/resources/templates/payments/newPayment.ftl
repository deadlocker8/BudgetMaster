<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#assign locale = static["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../navbar.ftl" as navbar>
        <@navbar.navbar "payments"/>

        <main>
            <div class="card main-card">
                <div class="container">
                    <div class="section center-align">
                        <div class="grey-text text-darken-4 headline"><#if payment.getID()??>${locale.getString("title.payment.edit")}<#else>${locale.getString("title.payment.new")}</#if></div>
                    </div>
                </div>
                <div class="container">
                    <#import "../validation.ftl" as validation>
                    <form name="NewPayment" action="/payments/newPayment" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="ID" value="<#if payment.getID()??>${payment.getID()}</#if>">

                        <#-- isPayment switch -->
                        <div class="row">
                            <div class="s12 m12 l8 offset-l2 center-align">
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
                                <input id="payment-amount" type="text" <@validation.validation "amount"/> value="<#if payment.getAmount()??>${payment.getAmount()}</#if>">
                                <label for="payment-amount">${locale.getString("payment.new.label.amount")}</label>
                            </div>
                            <input type="hidden" id="hidden-payment-amount" name="amount" value="<#if payment.getAmount()??>${payment.getAmount()}</#if>">
                        </div>

                        <#-- category -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <select id="payment-category" name="category" <@validation.validation "category"/>>
                                    <#list categories as category>
                                        <#if payment.getCategory()?? && payment.getCategory() == category>
                                            <option selected value="${category.getID()}">${category.getName()}</option>
                                        <#else>
                                            <#if category.getType() == "NONE">
                                                <option selected value="${category.getID()}">${category.getName()}</option>
                                            <#else>
                                                <option value="${category.getID()}">${category.getName()}</option>
                                            </#if>
                                        </#if>
                                    </#list>
                                </select>
                                <label for="payment-category">${locale.getString("payment.new.label.category")}</label>
                            </div>
                            <div id="hidden-payment-tags"></div>
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
                                <div id="payment-chips" class="chips chips-placeholder chips-autocomplete">
                                    <#if payment.getTags()??>
                                        <#list payment.getTags() as tag>
                                            <div class="chip">
                                                ${tag.getName()}
                                                <i class="close material-icons">close</i>
                                            </div>
                                        </#list>
                                    </#if>
                                </div>
                            </div>
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
                            <div id="hidden-payment-tags"></div>
                        </div>
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