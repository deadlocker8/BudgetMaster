<#import "/spring.ftl" as s>

<#macro buttonNew>
    <a href="<@s.url '/templates/newTemplate'/>" class="waves-effect waves-light btn budgetmaster-blue"><i
                class="material-icons left">add</i>${locale.getString("title.template.new")}</a>
</#macro>

<#macro buttons>
    <div class="row valign-wrapper">
        <div class="col s12 center-align">
            <@buttonNew/>
        </div>
    </div>
</#macro>

<#macro listTemplates templates isEditable>
    <div class="container">
        <div class="row">
            <div class="col s12">
                <ul class="collapsible expandable z-depth-2">
                    <#list templates as template>
                        <li>
                            <div class="collapsible-header bold">
                                <@templateHeader template/>
                                <div class="collapsible-header-button">
                                    <#if isEditable>
                                        <a href="<@s.url '/templates/${template.ID?c}/edit'/>" class="btn-flat no-padding text-color"><i class="material-icons left">edit</i></a>
                                        <a href="<@s.url '/templates/${template.ID?c}/requestDelete'/>" class="btn-flat no-padding text-color"><i class="material-icons left">delete</i></a>
                                    <#else>
                                        <a href="<@s.url '/templates/${template.ID?c}/select'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">note_add</i>${locale.getString("title.transaction.new", locale.getString("title.transaction.new.normal"))}</a>
                                    </#if>
                                </div>
                            </div>
                            <div class="collapsible-body">
                                <div class="row no-margin-bottom">
                                    <table class="table-template-content text-color">
                                        <@templateName template/>
                                        <@templateAmount template/>
                                        <@templateCategory template/>
                                        <@templateDescription template/>
                                        <@templateTags template/>
                                        <@templateAccount template/>
                                        <@templateTransferAccount template/>
                                    </table>
                                </div>
                            </div>
                        </li>
                    </#list>
                </ul>
            </div>
        </div>
    </div>
</#macro>

<#macro templateHeader template>
    <#if template.getTransferAccount()??>
        <i class="material-icons">swap_horiz</i>
    <#else>
        <i class="material-icons">payment</i>
    </#if>
    <div class="truncate template-header-name">${template.getTemplateName()}</div>
</#macro>

<#macro templateName template>
    <#if template.getName()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.name")}</td>
            <td>${template.getName()}</td>
        </tr>
    </#if>
</#macro>

<#macro templateAmount template>
    <#if template.getAmount()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.amount")}</td>
            <td>${currencyService.getCurrencyString(template.getAmount())}</td>
        </tr>
    </#if>
</#macro>

<#macro templateCategory template>
    <#if template.getCategory()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.category")}</td>
            <td>${categoriesFunctions.getCategoryName(template.category)}</td>
        </tr>
    </#if>
</#macro>

<#macro templateDescription template>
    <#if template.getDescription()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.description")}</td>
            <td>${template.getDescription()}</td>
        </tr>
    </#if>
</#macro>

<#macro templateTags template>
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
</#macro>

<#macro templateAccount template>
    <#if template.getAccount()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.account")}</td>
            <td>${template.getAccount().getName()}</td>
        </tr>
    </#if>
</#macro>

<#macro templateTransferAccount template>
    <#if template.getTransferAccount()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.transfer.account")}</td>
            <td>${template.getTransferAccount().getName()}</td>
        </tr>
    </#if>
</#macro>