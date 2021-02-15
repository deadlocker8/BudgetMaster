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
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "accounts" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${title}</div>
                    </div>
                </div>
                <div class="container">
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="NewAccount" action="<@s.url '/accounts/newAccount'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="<#if account.getID()??>${account.getID()?c}</#if>">
                        <input type="hidden" name="isSelected" value="<#if account.isSelected()??>${account.isSelected()?c}</#if>">
                        <input type="hidden" name="isDefault" value="<#if account.isDefault()??>${account.isDefault()?c}</#if>">
                        <input type="hidden" name="isReadOnly" value="<#if account.isReadOnly()??>${account.isReadOnly()?c}</#if>">

                        <#-- name -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">edit</i>
                                <input id="account-name" type="text" name="name" <@validation.validation "name"/> value="<#if account.getName()??>${account.getName()}</#if>">
                                <label for="account-name">${locale.getString("account.new.label.name")}</label>
                            </div>
                        </div>
                        <br>

                        <#-- buttons -->
                        <div class="row hide-on-small-only">
                            <div class="col s6 right-align">
                                <a href="<@s.url '/accounts'/>" class="waves-effect waves-light btn background-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                            </div>

                            <div class="col s6 left-align">
                                <button id="button-save-account" class="btn waves-effect waves-light background-blue" type="submit" name="action">
                                    <i class="material-icons left">save</i>${locale.getString("save")}
                                </button>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <a href="<@s.url '/accounts'/>" class="waves-effect waves-light btn background-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                                </div>
                            </div>
                            <div class="row center-align">
                                <div class="col s12">
                                    <button id="button-save-account" class="btn waves-effect waves-light background-blue" type="submit" name="buttonSave">
                                        <i class="material-icons left">save</i>${locale.getString("save")}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/accounts.js'/>"></script>
    </@header.body>
</html>