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

<#macro listTemplatesAsCards templates>
    <div class="row">
        <#list templates as template>
            <div class="col s12 m6 l4">
                <div class="card budgetmaster-grey card-template">
                    <div class="card-content">
                        <@templateCardTitle template/>

                        <table class="table-template-content">
                            <@templateCardName template/>
                            <@templateCardAmount template/>
                            <@templateCardCategory template/>
                            <@templateCardDescription template/>
                            <@templateCardTags template/>
                            <@templateCardAccount template/>
                            <@templateCardTransferAccount template/>
                        </table>
                    </div>
                    <div class="card-action center-align card-delimiter">
                        <a href="<@s.url '/templates/${template.ID?c}/select'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">note_add</i>${locale.getString("title.transaction.new", locale.getString("title.transaction.new.normal"))}</a>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</#macro>

<#macro templateCardTitle template>
    <span class="card-title">
        <#if template.getTransferAccount()??>
            <i class="material-icons">swap_horiz</i>
        </#if>
        ${template.getTemplateName()}
    </span>
</#macro>

<#macro templateCardName template>
    <#if template.getName()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.name")}</td>
            <td>${template.getName()}</td>
        </tr>
    </#if>
</#macro>

<#macro templateCardAmount template>
    <#if template.getAmount()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.amount")}</td>
            <td>${currencyService.getCurrencyString(template.getAmount())}</td>
        </tr>
    </#if>
</#macro>

<#macro templateCardCategory template>
    <#if template.getCategory()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.category")}</td>
            <td>${categoriesFunctions.getCategoryName(template.category)}</td>
        </tr>
    </#if>
</#macro>

<#macro templateCardDescription template>
    <#if template.getDescription()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.description")}</td>
            <td>${template.getDescription()}</td>
        </tr>
    </#if>
</#macro>

<#macro templateCardTags template>
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

<#macro templateCardAccount template>
    <#if template.getAccount()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.account")}</td>
            <td>${template.getAccount().getName()}</td>
        </tr>
    </#if>
</#macro>

<#macro templateCardTransferAccount template>
    <#if template.getTransferAccount()??>
        <tr>
            <td class="template-content-label">${locale.getString("transaction.new.label.transfer.account")}</td>
            <td>${template.getTransferAccount().getName()}</td>
        </tr>
    </#if>
</#macro>