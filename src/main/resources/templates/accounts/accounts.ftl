<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.accounts')}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "accounts" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="material-icons">account_balance</i> ${locale.getString("menu.accounts")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="<@s.url '/accounts/newAccount'/>" id="button-new-account" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.account.new")}</a></div>
                <br>
                <div class="container account-container">
                    <table class="bordered">
                        <#list accounts as account>
                            <#if (account.getType().name() == "CUSTOM")>
                                <tr>
                                    <td>
                                        <#if account.isReadOnly()>
                                            <#assign toolTipText = locale.getString("account.tooltip.readonly.activate")/>
                                            <#assign lockIcon = '<i class="fas fa-lock"></i>'/>
                                            <div class="placeholder-icon"></div>
                                        <#else>
                                            <#assign toolTipText = locale.getString("account.tooltip.readonly.deactivate")/>
                                            <#assign lockIcon = '<i class="fas fa-lock-open"></i>'/>
                                            <a href="<@s.url '/accounts/${account.getID()?c}/setAsDefault'/>" class="btn-flat no-padding text-color tooltipped" data-position="left" data-tooltip="${locale.getString("account.tooltip.default")}"><i class="material-icons left"><#if account.isDefault()>star<#else>star_border</#if></i></a>
                                        </#if>

                                        <#if !account.isDefault()>
                                            <a href="<@s.url '/accounts/${account.getID()?c}/toggleReadOnly'/>" class="btn-flat no-padding text-color tooltipped" data-position="right" data-tooltip="${toolTipText}">${lockIcon}</a>
                                        </#if>
                                    </td>
                                    <td>${account.getName()}</td>
                                    <td>
                                        <a href="<@s.url '/accounts/${account.getID()?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                        <a href="<@s.url '/accounts/${account.getID()?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left no-margin">delete</i></a>
                                    </td>
                                </tr>
                            </#if>
                        </#list>
                    </table>
                    <#if accounts?size == 0>
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    </#if>
                </div>
            </div>
        </main>

        <#if currentAccount??>
            <@header.modalConfirmDelete title=locale.getString("info.title.account.delete") confirmUrl='/accounts' cancelUrlBase="/accounts" itemId=currentAccount.getID() confirmButtonText=locale.getString("info.button.account.delete")>
                <p>${locale.getString("info.text.account.delete", currentAccount.getName(), currentAccount.getReferringTransactions()?size)}</p>
            </@header.modalConfirmDelete>
        </#if>

        <#if accountNotDeletable??>
            <!-- warning account not deletable -->
            <div id="modalAccountNotDeletable" class="modal background-color">
                <div class="modal-content">
                    <h4>${locale.getString("info.title.account.delete")}</h4>
                    <p>${locale.getString("warning.text.account.delete", currentAccount.getName())}</p>
                </div>
                <div class="modal-footer background-color">
                    <a href="<@s.url '/accounts'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("ok")}</a>
                </div>
            </div>
        </#if>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/accounts.js'/>"></script>
    </@header.body>
</html>
