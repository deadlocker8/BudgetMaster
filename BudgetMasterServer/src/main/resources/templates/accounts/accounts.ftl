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

        <#import "../helpers/customSelectMacros.ftl" as customSelectMacros>

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
                        <form name="AccountsFilter" action="<@s.url '/accounts/applyFilter'/>" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <table class="bordered">
                                <thead class="hide-on-med-and-down">
                                    <tr>
                                        <th></th>
                                        <th>
                                            <div>${locale.getString("account.new.label.state")}</div>
                                            <div>
                                                <label>
                                                    <input type="checkbox" name="includeFullAccess" <#if accountsFilterConfiguration?? && accountsFilterConfiguration.includeFullAccess>checked</#if>/>
                                                    <span><i class="fas fa-edit placeholder-icon-right"></i>${locale.getString("account.state.full.access")}</span>
                                                </label>
                                            </div>
                                            <div>
                                                <label>
                                                    <input type="checkbox" name="includeReadOnly" <#if accountsFilterConfiguration?? && accountsFilterConfiguration.includeReadOnly>checked</#if>/>
                                                    <span><i class="fas fa-lock placeholder-icon-right"></i>${locale.getString("account.state.read.only")}</span>
                                                </label>
                                            </div>
                                            <div>
                                                <label>
                                                    <input type="checkbox" name="includeHidden" <#if accountsFilterConfiguration?? && accountsFilterConfiguration.includeHidden>checked</#if>/>
                                                    <span><i class="far fa-eye-slash placeholder-icon-right"></i>${locale.getString("account.state.hidden")}</span>
                                                </label>
                                            </div>
                                        </th>
                                        <th>
                                            <div>${locale.getString("account.new.label.endDate")}</div>
                                            <div>
                                                <label>
                                                    <input type="checkbox" name="includeWithEndDate" <#if accountsFilterConfiguration?? && accountsFilterConfiguration.includeWithEndDate>checked</#if>/>
                                                    <span><i class="fas fa-bell placeholder-icon-right"></i>${locale.getString("account.label.endDate.with")}</span>
                                                </label>
                                            </div>
                                            <div>
                                                <label>
                                                    <input type="checkbox" name="includeWithoutEndDate" <#if accountsFilterConfiguration?? && accountsFilterConfiguration.includeWithoutEndDate>checked</#if>/>
                                                    <span><i class="fas fa-bell-slash placeholder-icon-right"></i>${locale.getString("account.label.endDate.without")}</span>
                                                </label>
                                            </div>
                                        </th>
                                        <th>
                                            <div>${locale.getString("account.new.label.name")}</div>
                                            <div class="input-field">
                                                <input type="text" name="name" value="<#if accountsFilterConfiguration??>${accountsFilterConfiguration.name}</#if>"/>
                                            </div>
                                        </th>
                                        <th>
                                            <div>${locale.getString("transaction.new.label.description")}</div>
                                            <div class="input-field">
                                                <input type="text" name="description" value="<#if accountsFilterConfiguration??>${accountsFilterConfiguration.description}</#if>"/>
                                            </div>
                                        </th>
                                        <th class="vertical-align-middle">
                                            <button class="btn waves-effect waves-light background-green" type="submit" id="accounts-filter-button">
                                                <i class="fas fa-filter left"></i>${locale.getString("filter.apply")}
                                            </button>
                                        </th>
                                    </tr>
                                </thead>

                                <#list accounts as account>
                                    <#if (account.getType().name() == "CUSTOM")>
                                        <tr class="account-overview-row">
                                            <td><@customSelectMacros.accountIcon account account.getName() "text-blue"/></td>
                                            <td>
                                                <#if account.getAccountState().name() == "READ_ONLY">
                                                    <div class="placeholder-icon placeholder-icon-right"></div>
                                                    <i class="fas fa-lock placeholder-icon-right"></i>
                                                <#elseif account.getAccountState().name() == "HIDDEN">
                                                    <div class="placeholder-icon placeholder-icon-right"></div>
                                                    <i class="far fa-eye-slash placeholder-icon-right"></i>
                                                <#else>
                                                    <a href="<@s.url '/accounts/${account.getID()?c}/setAsDefault'/>" class="btn-flat no-padding text-default tooltipped" data-position="left" data-tooltip="${locale.getString("account.tooltip.default")}"><i class="material-icons left"><#if account.isDefault()>star<#else>star_border</#if></i></a>
                                                    <i class="fas fa-edit placeholder-icon-right"></i>
                                                </#if>
                                            </td>
                                            <td>
                                                <#if account.getEndDate()??>
                                                    <i class="fas fa-bell placeholder-icon-right"></i> ${dateService.getDateStringNormal(account.getEndDate())}
                                                <#else>
                                                    <div class="placeholder-icon"></div>
                                                </#if>
                                            </td>
                                            <td>${account.getName()}</td>
                                            <td>
                                                <div class="truncate account-description">
                                                    ${account.getDescription()}
                                                </div>
                                            </td>
                                            <td>
                                                <a href="<@s.url '/accounts/${account.getID()?c}/edit'/>" class="btn-flat no-padding text-default"><i class="material-icons left">edit</i></a>
                                                <@header.buttonFlat url='/accounts/' + account.ID?c + '/requestDelete' icon='delete' localizationKey='' classes="no-padding text-default button-request-delete-account" isDataUrl=true/>
                                            </td>
                                        </tr>
                                    </#if>
                                </#list>
                            </table>
                        </form>
                        <#if accounts?size == 0>
                            <div class="headline center-align">${locale.getString("placeholder")}</div>
                        </#if>
                    </div>
                </@header.content>
            </div>

            <div id="deleteModalContainerOnDemand"></div>
        </main>


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
