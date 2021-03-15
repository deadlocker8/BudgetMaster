<#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>

<#macro customCategorySelect categories selectedCategory inputClasses labelText>
    <div class="row">
        <div class="input-field ${inputClasses}">
            <i class="material-icons prefix">label</i>
            <label class="input-label" for="transaction-category">${labelText}</label>
            <div class="custom-select-wrapper category-select-wrapper" id="transaction-category">
                <div class="custom-select">
                    <div class="custom-select-trigger" tabindex="0"><div id="custom-select-selected-item"><#if selectedCategory??><@customSelectOptionCategoryContent selectedCategory "no-margin-left"/></#if></div>
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


                <input type="hidden" name="account" class="hidden-input-custom-select" <#if selectedAccount??>value="${selectedAccount.getID()?c}"</#if>/>
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