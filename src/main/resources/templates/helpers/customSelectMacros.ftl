<#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>

<#macro customCategorySelect categories selectedCategory inputClasses labelText>
    <div class="row">
        <div class="input-field ${inputClasses}">
            <i class="material-icons prefix">label</i>
            <label class="input-label" for="transaction-category">${labelText}</label>
            <div class="custom-select-wrapper category-select-wrapper" id="transaction-category">
                <div class="custom-select">
                    <div class="custom-select-trigger" tabindex="0"><div class="custom-select-selected-item"><#if selectedCategory??><@customSelectOptionCategoryContent selectedCategory "no-margin-left"/></#if></div>
                        <div class="custom-select-arrow"></div>
                    </div>
                    <div class="custom-select-options">
                        <#list categories as category>
                            <#if category.getType() == "REST">
                                <#continue>
                            </#if>

                            <#if selectedCategory??>
                                <#if selectedCategory.getID()?c == category.getID()?c>
                                    <@customSelectCategoryOption category true/>
                                <#else>
                                    <@customSelectCategoryOption category false/>
                                </#if>
                                <#continue>
                            </#if>

                            <#if category.getType() == "NONE">
                                <@customSelectCategoryOption category true/>
                                <#continue>
                            </#if>

                            <@customSelectCategoryOption category false/>
                        </#list>
                    </div>
                </div>

                <input type="hidden" name="category" class="hidden-input-custom-select" <#if selectedCategory??>value="${selectedCategory.getID()?c}"</#if>/>
            </div>
        </div>
    </div>
</#macro>

<#macro customAccountSelect selector inputName accounts selectedAccount inputClasses labelText>
    <div class="row">
        <div class="input-field ${inputClasses}">
            <i class="material-icons prefix">account_balance</i>
            <label class="input-label" for="transaction-account">${labelText}</label>
            <div class="custom-select-wrapper ${selector}" id="transaction-account">
                <div class="custom-select">
                    <div class="custom-select-trigger" tabindex="0"><div class="custom-select-selected-item"><#if selectedAccount??><@customSelectOptionAccountContent selectedAccount "no-margin-left"/></#if></div>
                        <div class="custom-select-arrow"></div>
                    </div>
                    <div class="custom-select-options">
                        <#list accounts as account>
                            <#if (account.getType().name() != "CUSTOM")>
                                <#continue>
                            </#if>

                            <#if selectedAccount??>
                                <#if selectedAccount.getID()?c == account.getID()?c>
                                    <@customSelectAccountOption account true/>
                                <#else>
                                    <@customSelectAccountOption account false/>
                                </#if>
                                <#continue>
                            </#if>

                            <@customSelectAccountOption account false/>
                        </#list>
                    </div>
                </div>

                <input type="hidden" name="${inputName}" class="hidden-input-custom-select" <#if selectedAccount??>value="${selectedAccount.getID()?c}"</#if>/>
            </div>
        </div>
    </div>
</#macro>

<#macro customSelectCategoryOption category isSelected>
    <div class="custom-select-option <#if isSelected>selected</#if>" data-value="${category.getID()?c}">
        <@customSelectOptionCategoryContent category/>
    </div>
</#macro>

<#macro customSelectOptionCategoryContent category classes="" datasetValue="">
    <@categoriesFunctions.categoryCircle category "category-circle-small ${classes}" datasetValue=""/>
    <span class="custom-select-item-name">${categoriesFunctions.getCategoryName(category)}</span>
</#macro>

<#macro customSelectAccountOption account isSelected>
    <div class="custom-select-option <#if isSelected>selected</#if>" data-value="${account.getID()?c}">
        <@customSelectOptionAccountContent account/>
    </div>
</#macro>

<#macro customSelectOptionAccountContent account classes="" datasetValue="">
    <@accountIcon account "category-circle-small ${classes}" datasetValue=""/>
    <span class="custom-select-item-name">${account.getName()}</span>
</#macro>

<#macro accountIcon account classes="" datasetValue="">
    <div class="category-circle ${classes} category-square <#if account.getIcon()?? == false>account-square-border</#if>" <#if datasetValue?has_content>data-value="${account.getID()}"</#if>>
        <#if account.getIcon()??>
            <img src="${account.getIcon().getBase64EncodedImage()}" class="account-select-icon"/>
        <#else>
            <span class="text-blue">
                ${account.getName()?capitalize[0]}
            </span>
        </#if>
    </div>
</#macro>