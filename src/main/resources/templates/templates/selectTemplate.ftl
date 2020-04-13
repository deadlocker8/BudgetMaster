<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.header "BudgetMaster"/>
        <@header.style "templates"/>
        <#import "/spring.ftl" as s>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "templates" settings/>

        <#import "templateFunctions.ftl" as templateFunctions>
        <#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.templates")}</div>
                    </div>
                </div>
                <br>
                <div class="center-align"><a href="<@s.url '/templates'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">edit</i>${locale.getString("home.menu.templates.action.manage")}</a></div>
                <br>
                <#if templates?size == 0>
                    <div class="container">
                        <div class="headline center-align">${locale.getString("placeholder")}</div>
                    </div>
                <#else>
                    <div class="row">
                        <#list templates as template>
                            <div class="col s12 m6 l4">
                                <div class="card budgetmaster-grey card-template">
                                    <div class="card-content">
                                        <span class="card-title">${template.getTemplateName()}</span>
                                        <table class="table-template-content">
                                            <#if template.getName()??>
                                                <tr>
                                                    <td class="template-content-label">${locale.getString("transaction.new.label.name")}</td>
                                                    <td>${template.getName()}</td>
                                                </tr>
                                            </#if>

                                            <#if template.getAmount()??>
                                                <tr>
                                                    <td class="template-content-label">${locale.getString("transaction.new.label.amount")}</td>
                                                    <td>${currencyService.getCurrencyString(template.getAmount())}</td>
                                                </tr>
                                            </#if>

                                            <#if template.getCategory()??>
                                                <tr>
                                                    <td class="template-content-label">${locale.getString("transaction.new.label.category")}</td>
                                                    <td>${categoriesFunctions.getCategoryName(template.category)}</td>
                                                </tr>
                                            </#if>

                                            <#if template.getDescription()??>
                                                <tr>
                                                    <td class="template-content-label">${locale.getString("transaction.new.label.description")}</td>
                                                    <td>${template.getDescription()}</td>
                                                </tr>
                                            </#if>

                                            <#if template.getTags()?? && template.getTags()?size gt 0>
                                                <tr>
                                                    <td class="template-content-label">${locale.getString("transaction.new.label.tags")}</td>
                                                    <td class="chips-placeholder">
                                                        <#list template.getTags() as tag>
                                                            <div class="chip">${tag.getName()}</div>
                                                        </#list>
                                                    </td>
                                                </tr>
                                            </#if>

                                            <#if template.getAccount()??>
                                                <tr>
                                                    <td class="template-content-label">${locale.getString("transaction.new.label.account")}</td>
                                                    <td>${template.getAccount().getName()}</td>
                                                </tr>
                                            </#if>

                                            <#if template.getTransferAccount()??>
                                                <tr>
                                                    <td class="template-content-label">${locale.getString("transaction.new.label.transfer.account")}</td>
                                                    <td>${template.getTransferAccount().getName()}</td>
                                                </tr>
                                            </#if>
                                        </table>
                                    </div>
                                    <div class="card-action center-align card-delimiter">
                                        <a href="<@s.url '/templates/${template.ID?c}/select'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">note_add</i>${locale.getString("title.transaction.new", locale.getString("title.transaction.new.normal"))}</a>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </div>
                </#if>
            </div>
        </main>

        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/templates.js'/>"></script>
    </body>
</html>