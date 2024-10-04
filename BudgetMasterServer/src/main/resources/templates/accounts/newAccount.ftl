<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>

        <#if account.getID()??>
            <#assign title=locale.getString("title.account.edit")/>
        <#else>
            <#assign title=locale.getString("title.account.new")/>
        </#if>

        <@header.header "BudgetMaster - ${title}"/>
        <@header.style "iconSelect"/>
        <@header.style "datepicker"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "accounts" settings/>

        <#import "../helpers/customSelectMacros.ftl" as customSelectMacros>
        <#import "../helpers/iconSelect.ftl" as iconSelectMacros>
        <#import "../helpers/fontColorPicker.ftl" as fontColorPickerMacros>

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
                    <form name="NewAccount" action="<@s.url '/accounts/newAccount'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="<#if account.getID()??>${account.getID()?c}</#if>">
                        <input type="hidden" name="isSelected" value="<#if account.isSelected()??>${account.isSelected()?c}</#if>">
                        <input type="hidden" name="isDefault" value="<#if account.isDefault()??>${account.isDefault()?c}</#if>">

                        <#-- name -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">edit</i>
                                <input id="account-name" type="text" name="name" <@validation.validation "name"/> value="<#if account.getName()??>${account.getName()}</#if>">
                                <label for="account-name">${locale.getString("account.new.label.name")}</label>
                            </div>
                        </div>

                        <#-- description -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">article</i>
                                <textarea id="account-description" class="materialize-textarea" name="description" data-length="250" <@validation.validation "description"/>><#if account.getDescription()??>${account.getDescription()}</#if></textarea>
                                <label class="input-label" for="account-description">${locale.getString("transaction.new.label.description")}</label>
                            </div>
                        </div>

                        <#-- icon -->
                        <#if account.getIconReference()?? && (account.getIconReference().isImageIcon() || account.getIconReference().isBuiltinIcon())>
                            <#assign initialBackgroundClasses='category-square'/>
                        <#else>
                            <#assign initialBackgroundClasses='category-square account-square-border'/>
                        </#if>
                        <#assign backgroundClasses='category-square account-square-border'/>

                        <@iconSelectMacros.iconSelect id="account-icon" item=account showBackground=false initialBackgroundClasses=initialBackgroundClasses backgroundClasses=backgroundClasses/>

                        <#-- font color -->
                        <@fontColorPickerMacros.fontColorPicker account/>

                        <#-- state -->
                        <#if account.getAccountState()??>
                            <#assign selectedState=account.getAccountState()>
                        <#else>
                            <#assign selectedState=availableAccountStates[0]>
                        </#if>
                        <@customSelectMacros.customAccountStateSelect "account-state-select-wrapper" "accountState" availableAccountStates selectedState "col s12 m12 l8 offset-l2" locale.getString("account.new.label.state") "account-state"/>

                        <#-- end date -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <#if account.getEndDate()??>
                                    <#assign startDate = dateService.getLongDateString(account.getEndDate())/>
                                    <#assign accountEndDate = dateService.getLongDateString(account.getEndDate())/>
                                <#else>
                                    <#assign startDate = dateService.getLongDateString(today)/>
                                    <#assign accountEndDate = ""/>
                                </#if>

                                <i class="material-icons prefix">notifications</i>
                                <input id="account-datepicker" type="text" class="datepicker" name="endDate" value="${accountEndDate}">
                                <label class="input-label" for="account-datepicker">${locale.getString("account.new.label.endDate")}</label>
                            </div>
                        </div>

                        <script>
                            startDate = "${startDate}".split(".");
                            startDate = new Date(startDate[2], startDate[1]-1, startDate[0]);
                        </script>

                        <br>

                        <#-- buttons -->
                        <div class="row hide-on-small-only">
                            <div class="col s6 right-align">
                                <@header.buttonLink url='/accounts' icon='clear' localizationKey='cancel' id='button-cancel-save-account' color='red'/>
                            </div>

                            <div class="col s6 left-align">
                                <@header.buttonSubmit name='action' icon='save' localizationKey='save' id='button-save-account' color='green'/>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <@header.buttonLink url='/accounts' icon='clear' localizationKey='cancel' id='button-cancel-save-account' color='red'/>
                                </div>
                            </div>
                            <div class="row center-align">
                                <div class="col s12">
                                    <@header.buttonSubmit name='action' icon='save' localizationKey='save' id='button-save-account' color='green'/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                </@header.content>
            </div>
        </main>

        <@iconSelectMacros.modalIconSelect idToFocusOnClose="account-name" item=account/>

        <#import "../helpers/globalDatePicker.ftl" as datePicker>
        <@datePicker.datePickerLocalization/>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/webjars/vanilla-picker/2.12.3/dist/vanilla-picker.min.js'/>"></script>
        <script src="<@s.url '/js/accounts.js'/>"></script>
        <script src="<@s.url '/js/iconSelect.js'/>"></script>
        <script src="<@s.url '/js/fontColorPicker.js'/>"></script>
    </@header.body>
</html>