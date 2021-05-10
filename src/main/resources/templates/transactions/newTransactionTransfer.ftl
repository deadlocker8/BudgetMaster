<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <#assign title = locale.getString("title.transaction.new.transfer")/>
        <#if isEdit>
            <#assign title=locale.getString("title.transaction.edit", title)/>
        <#else>
            <#assign title=locale.getString("title.transaction.new", title)/>
        </#if>

        <@header.header "BudgetMaster - ${title}"/>
        <@header.style "transactions"/>
        <@header.style "datepicker"/>
        <@header.style "collapsible"/>
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
                        <form name="NewTransaction" action="<@s.url '/transactions/newTransaction/transfer'/>" method="post" onsubmit="return validateForm()">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <!-- only set ID for transactions not templates, otherwise the input is filled with the template ID and saving the transaction
                            may then override an existing transactions if the ID is also already used in transactions table -->
                            <input type="hidden" name="ID" value="<#if transaction.class.simpleName == "Transaction" && transaction.getID()??>${transaction.getID()?c}</#if>">
                            <input type="hidden" name="isExpenditure" value="true">
                            <input type="hidden" name="previousType" value="<#if previousType??>${previousType.name()}</#if>">

                            <#-- name -->
                            <@newTransactionMacros.transactionName transaction suggestionsJSON/>

                            <#-- amount -->
                            <@newTransactionMacros.transactionAmount transaction/>

                            <#-- category -->
                            <@customSelectMacros.customCategorySelect categories transaction.getCategory() "col s12 m12 l8 offset-l2" locale.getString("transaction.new.label.category")/>

                            <#-- date -->
                            <@newTransactionMacros.transactionStartDate transaction currentDate/>

                            <#-- description -->
                            <@newTransactionMacros.transactionDescription transaction/>

                            <#-- tags -->
                            <@newTransactionMacros.transactionTags transaction/>

                            <#-- account -->
                            <#if transaction.getAccount()??>
                                <#assign selectedAccount = transaction.getAccount()/>
                            <#else>
                                <#assign selectedAccount = helpers.getCurrentAccountOrDefault()/>
                            </#if>
                            <@customSelectMacros.customAccountSelect "account-select-wrapper" "account" accounts selectedAccount "col s12 m12 l8 offset-l2" locale.getString("transaction.new.label.account") "transaction-account"/>

                            <#-- transfer account -->
                            <#if transaction.getTransferAccount()??>
                                <#assign selectedTransferAccount = transaction.getTransferAccount()/>
                            <#else>
                                <#assign selectedTransferAccount = helpers.getCurrentAccountOrDefault()/>
                            </#if>
                            <@customSelectMacros.customAccountSelect "transfer-account-select-wrapper" "transferAccount" accounts selectedTransferAccount "col s12 m12 l8 offset-l2" locale.getString("transaction.new.label.transfer.account") "transaction-destination-account"/>

                            <br>
                            <#-- buttons -->
                            <@newTransactionMacros.buttons '/transactions'/>
                            <@newTransactionMacros.buttonTransactionActions isEdit true previousType??/>
                        </form>

                        <div id="saveAsTemplateModalContainer"></div>

                        <div id="changeTransactionTypeModalContainer"></div>
                    </div>
                </@header.content>
            </div>
        </main>

        <!-- Pass localization to JS -->
        <#import "../helpers/globalDatePicker.ftl" as datePicker>
        <@datePicker.datePickerLocalization/>

        <script>
            createTemplateWithErrorInForm = '${locale.getString("save.as.template.errorsInForm")}';
            templateNameEmptyValidationMessage = "${locale.getString("warning.empty.transaction.name")}";
            templateNameDuplicateValidationMessage = "${locale.getString("warning.duplicate.template.name")}";
        </script>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/libs/spectrum.js'/>"></script>
        <script src="<@s.url '/js/helpers.js'/>"></script>
        <script src="<@s.url '/js/transactions.js'/>"></script>
        <script src="<@s.url '/js/transactionActions.js'/>"></script>
        <script src="<@s.url '/js/templates.js'/>"></script>
    </@header.body>
</html>
