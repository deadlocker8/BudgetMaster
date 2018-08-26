<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header "BudgetMaster"/>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../navbar.ftl" as navbar>
        <@navbar.navbar "accounts"/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.accounts")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="/accounts/newAccount" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.account.new")}</a></div>
                <br>
                <div class="container">
                    <table class="bordered">
                        <#list accounts as account>
                        <tr>
                            <td>${account.getName()}</td>
                            <td>
                                <a href="/accounts/${account.getID()}/edit" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                <a href="/accounts/${account.getID()}/requestDelete" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
                            </td>
                        </tr>
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
                    <a href="/accounts" class="modal-action modal-close waves-effect waves-red btn-flat ">${locale.getString("cancel")}</a>
                    <a href="/accounts/${currentAccount.getID()}/delete" class="modal-action modal-close waves-effect waves-green btn-flat ">${locale.getString("info.button.account.delete")}</a>
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
                    <a href="/accounts" class="modal-action modal-close waves-effect waves-green btn-flat text-color">${locale.getString("ok")}</a>
                </div>
            </div>
        </#if>

        <!--  Scripts-->
        <#import "../scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="/js/accounts.js"></script>
    </body>
</html>