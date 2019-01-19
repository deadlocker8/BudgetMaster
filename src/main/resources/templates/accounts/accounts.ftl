<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "accounts"/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.accounts")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="<@s.url '/accounts/newAccount'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.account.new")}</a></div>
                <br>
                <div class="container">
                    <table class="bordered">
                        <#list accounts as account>
                            <#if (account.getType().name() == "CUSTOM")>
                                <tr>
                                    <td>${account.getName()}</td>
                                    <td>
                                        <a href="<@s.url '/accounts/${account.getID()?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                        <a href="<@s.url '/accounts/${account.getID()?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
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
            <!-- confirm delete modal -->
            <div id="modalConfirmDelete" class="modal background-color">
                <div class="modal-content">
                    <h4>${locale.getString("info.title.account.delete")}</h4>
                    <p>${locale.getString("info.text.account.delete", currentAccount.getName(), currentAccount.getReferringTransactions()?size)}</p>
                </div>
                <div class="modal-footer background-color">
                    <a href="<@s.url '/accounts'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
                    <a href="<@s.url '/accounts/${currentAccount.getID()?c}/delete'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("info.button.account.delete")}</a>
                </div>
            </div>
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
    </body>
</html>