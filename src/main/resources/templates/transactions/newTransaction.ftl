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
        <@navbar.navbar "transactions" settings/>

        <#import "newTransactionMacros.ftl" as newTransactionMacros>

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
                        <@newTransactionMacros.isExpenditureSwitch transaction/>

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
                        <@newTransactionMacros.category categories transaction/>

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
                        <@newTransactionMacros.account accounts transaction/>

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
                        <@newTransactionMacros.repeatingModifier transaction/>

                        <#-- repeating end option -->
                        <@newTransactionMacros.repeatingEndOption transaction currentDate/>

                        <br>

                        <#-- buttons -->
                        <@newTransactionMacros.buttons/>
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
