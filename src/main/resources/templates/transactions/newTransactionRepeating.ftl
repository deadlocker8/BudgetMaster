<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <#assign title = locale.getString("title.transaction.new.repeating.long")/>
        <#if isEdit>
            <#assign title=locale.getString("title.transaction.edit", title)/>
        <#else>
            <#assign title=locale.getString("title.transaction.new", title)/>
        </#if>

        <@header.header "BudgetMaster - ${title}"/>
        <@header.style "transactions"/>
        <@header.style "datepicker"/>
        <@header.style "categories"/>
        <@header.style "categorySelect"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "transactions" settings/>

        <#import "newTransactionMacros.ftl" as newTransactionMacros>
        <#import "../helpers/customSelectMacros.ftl" as customSelectMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${title}</div>
                    </div>
                </div>

                <@header.content>
                    <div class="container">
                        <#import "../helpers/validation.ftl" as validation>
                        <form name="NewTransaction" action="<@s.url '/transactions/newTransaction/repeating'/>" method="post" onsubmit="return validateForm()">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" name="ID" value="<#if transaction.getID()??>${transaction.getID()?c}</#if>">
                            <input type="hidden" name="isRepeating" value="${transaction.isRepeating()?c}">
                            <input type="hidden" name="previousType" value="<#if previousType??>${previousType.name()}</#if>">

                            <#-- isPayment switch -->
                            <@newTransactionMacros.isExpenditureSwitch transaction/>

                            <#-- name -->
                            <@newTransactionMacros.transactionName transaction suggestionsJSON/>

                            <#-- amount -->
                            <@newTransactionMacros.transactionAmount transaction/>

                            <#-- category -->
                            <@customSelectMacros.customSelect categories transaction.getCategory() "col s12 m12 l8 offset-l2" locale.getString("transaction.new.label.category")/>

                            <#-- date -->
                            <@newTransactionMacros.transactionStartDate transaction currentDate/>

                            <#-- description -->
                            <@newTransactionMacros.transactionDescription transaction/>

                            <#-- tags -->
                            <@newTransactionMacros.transactionTags transaction/>

                            <#-- account -->
                            <#if transaction.getAccount()??>
                                <@newTransactionMacros.account accounts transaction.getAccount() "transaction-account" "account" locale.getString("transaction.new.label.account") false/>
                            <#else>
                                <@newTransactionMacros.account accounts helpers.getCurrentAccountOrDefault() "transaction-account" "account" locale.getString("transaction.new.label.account") false/>
                            </#if>

                            <#-- repeating options -->
                            <@newTransactionMacros.transactionRepeating transaction currentDate/>
                            <br>
                            <#-- buttons -->
                            <@newTransactionMacros.buttons "/transactions"/>
                            <@newTransactionMacros.buttonTransactionActions isEdit false previousType??/>
                        </form>

                        <div id="changeTransactionTypeModalContainer"></div>
                    </div>
                </@header.content>
            </div>
        </main>

        <!-- Pass localization to JS -->
        <#import "../helpers/globalDatePicker.ftl" as datePicker>
        <@datePicker.datePickerLocalization/>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/libs/spectrum.js'/>"></script>
        <script src="<@s.url '/js/helpers.js'/>"></script>
        <script src="<@s.url '/js/transactions.js'/>"></script>
        <script src="<@s.url '/js/transactionActions.js'/>"></script>
        <script src="<@s.url '/js/customSelect.js'/>"></script>
    </@header.body>
</html>
