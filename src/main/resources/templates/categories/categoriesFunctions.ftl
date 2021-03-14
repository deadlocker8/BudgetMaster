<#import "../helpers/header.ftl" as header>

<#function getCategoryName category>
    <#if category?? && category.getName()??>
        <#if category.getType().name() == "NONE">
            <#return locale.getString("category.none")>
        <#elseif category.getType().name() == "REST">
            <#return locale.getString("category.rest")>
        <#else>
            <#return category.getName()>
        </#if>
    <#else>
        <#return "">
    </#if>
</#function>

<#macro categoryCircle category classes="" datasetValue="">
    <#assign categoryName=getCategoryName(category)>

    <div class="category-circle ${classes} <#if settings.getShowCategoriesAsCircles()?? && settings.getShowCategoriesAsCircles() == false>category-square</#if>" style="background-color: ${category.color}" <#if datasetValue?has_content>data-value="${category.getID()}"</#if>>
        <span style="color: ${category.getAppropriateTextColor()}">
            <#if category.getIcon()?has_content>
                <i class="${category.getIcon()}"></i>
            <#else>
                ${categoryName?capitalize[0]}
            </#if>
        </span>
    </div>
</#macro>

<#macro modalIconSelect>
    <div id="modalIconSelect" class="modal modal-fixed-footer background-color">
        <div class="modal-content">
            <div class="row no-margin-bottom">
                <div class="input-field col s12 m12 l8 offset-l2">
                    <i class="material-icons prefix">search</i>
                    <input id="searchIcons" type="text" onchange="searchCategoryIcons();" onkeypress="searchCategoryIcons();" onpaste="searchCategoryIcons()" oninput="searchCategoryIcons();">
                    <label for="searchIcons">${locale.getString("search")}</label>
                </div>
            </div>
            <div class="row">
                <div class="col s12 center-align" id="numberOfIcons"><span id="numberOfMatchingIcons">${fontawesomeIcons?size?c}</span>/${fontawesomeIcons?size?c} ${locale.getString("category.new.icons")}</div>
            </div>

            <hr>

            <div class="row">
                <#list fontawesomeIcons as icon>
                    <@categoryIconOption icon/>
                </#list>
            </div>

        </div>
        <div class="modal-footer background-color">
            <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
            <@header.buttonLink url='' icon='done' id='button-category-icon-confirm' localizationKey='ok' color='green' classes='modal-action modal-close text-white' noUrl=true/>
        </div>
    </div>
</#macro>

<#macro categoryIconOption icon>
    <div class="col s4 m2 l2 category-icon-option-column">
        <div class="category-icon-option">
            <i class="category-icon-option-icon ${icon}"></i>
            <div class="category-icon-option-name truncate">${icon}</div>
        </div>
    </div>
</#macro>