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

                <@header.content>
                    <br>
                    <div class="center-align"><@header.buttonLink url='/accounts/newAccount' icon='add' localizationKey='title.account.new' id='button-new-account'/></div>
                    <br>
                    <div class="container account-container">
                    <table class="bordered">
                        <#list accounts as account>
                            <#if (account.getType().name() == "CUSTOM")>
                                <tr class="account-overview-row">
                                    <td>
                                        <#if account.getAccountState().name() == "READ_ONLY">
                                            <div class="placeholder-icon"></div>
                                            <i class="fas fa-lock"></i>
                                        <#elseif account.getAccountState().name() == "HIDDEN">
                                            <div class="placeholder-icon"></div>
                                            <i class="far fa-eye-slash"></i>
                                        <#else>
                                            <a href="<@s.url '/accounts/${account.getID()?c}/setAsDefault'/>" class="btn-flat no-padding text-default tooltipped" data-position="left" data-tooltip="${locale.getString("account.tooltip.default")}"><i class="material-icons left"><#if account.isDefault()>star<#else>star_border</#if></i></a>
                                            <i class="fas fa-edit"></i>
                                        </#if>
                                    </td>
                                    <td><@header.entityIcon entity=account classes="account-icon"/></td>
                                    <td>${account.getName()}</td>
                                    <td>
                                        <a href="<@s.url '/accounts/${account.getID()?c}/edit'/>" class="btn-flat no-padding text-default"><i class="material-icons left">edit</i></a>
                                        <a href="<@s.url '/accounts/${account.getID()?c}/requestDelete'/>" class="btn-flat no-padding text-default"><i class="material-icons left no-margin">delete</i></a>
                                    </td>
                                </tr>
                            </#if>
                        </#list>
                    </table>
                    <#if accounts?size == 0>
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    </#if>
                </div>
                </@header.content>
            </div>
        </main>

        <#if currentAccount??>
            <@header.modalConfirmDelete title=locale.getString("info.title.account.delete") confirmUrl='/accounts' cancelUrlBase="/accounts" itemId=currentAccount.getID() confirmButtonTextKey="info.button.account.delete">
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
                    <@header.buttonLink url='/accounts' icon='done' localizationKey='ok' color='green' id='buttonCloseWhatsNew' classes='modal-action modal-close text-white'/>
                </div>
            </div>
        </#if>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/accounts.js'/>"></script>
    </@header.body>
</html>
