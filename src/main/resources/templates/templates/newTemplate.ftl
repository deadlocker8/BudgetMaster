<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <#if isEdit>
            <#assign title=locale.getString("title.template.edit")/>
        <#else>
            <#assign title=locale.getString("title.template.new")/>
        </#if>

        <@header.header "BudgetMaster - ${title}"/>
        <@header.style "transactions"/>
        <@header.style "datepicker"/>
        <@header.style "categories"/>
        <@header.style "collapsible"/>
        <@header.style "categorySelect"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "templates" settings/>

        <#import "../transactions/newTransactionMacros.ftl" as newTransactionMacros>
        <#import "templateFunctions.ftl" as templateFunctions>

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
                    <form name="NewTemplate" action="<@s.url '/templates/newTemplate'/>" method="post" onsubmit="return validateForm(true)">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="<#if template.getID()??>${template.getID()?c}</#if>">

                        <#-- isPayment switch -->
                        <@newTransactionMacros.isExpenditureSwitch template/>

                        <#-- template name -->
                        <@templateFunctions.templateNameInput template/>

                        <#-- name -->
                        <@newTransactionMacros.transactionName template suggestionsJSON/>

                        <#-- amount -->
                        <@newTransactionMacros.transactionAmount template/>

                        <#-- category -->
                        <@newTransactionMacros.categorySelect categories template.getCategory() "col s12 m12 l8 offset-l2" locale.getString("transaction.new.label.category")/>

                        <#-- description -->
                        <@newTransactionMacros.transactionDescription template/>

                        <#-- tags -->
                        <@newTransactionMacros.transactionTags template/>

                        <#-- account -->
                        <#if template.getAccount()??>
                            <@templateFunctions.templateIncludeAccountCheckbox "include-account" "includeAccount" locale.getString('template.checkbox.include.account') true/>
                            <@newTransactionMacros.account accounts template.getAccount() "transaction-account" "account" "" false/>
                        <#else>
                            <@templateFunctions.templateIncludeAccountCheckbox "include-account" "includeAccount" locale.getString('template.checkbox.include.account') false/>
                            <@newTransactionMacros.account accounts helpers.getCurrentAccountOrDefault() "transaction-account" "account" "", true/>
                        </#if>

                        <#-- transfer account -->
                        <#if template.getTransferAccount()??>
                            <@templateFunctions.templateIncludeAccountCheckbox "include-transfer-account" "includeTransferAccount" locale.getString('template.checkbox.include.account.transfer') true/>
                            <@newTransactionMacros.account accounts template.getTransferAccount() "transaction-transfer-account" "transferAccount" "" false/>
                        <#else>
                            <@templateFunctions.templateIncludeAccountCheckbox "include-transfer-account" "includeTransferAccount" locale.getString('template.checkbox.include.account.transfer') false/>
                            <@newTransactionMacros.account accounts helpers.getCurrentAccountOrDefault() "transaction-transfer-account" "transferAccount" "", true/>
                        </#if>

                        <br>
                        <#-- buttons -->
                        <@newTransactionMacros.buttons "/templates"/>
                    </form>
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
        <script src="<@s.url '/js/categorySelect.js'/>"></script>
        <script src="<@s.url '/js/templates.js'/>"></script>
    </@header.body>
</html>
