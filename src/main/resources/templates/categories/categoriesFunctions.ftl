<#import "../helpers/header.ftl" as header>
<#import "/spring.ftl" as s>

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

<#macro categoryCircle category classes="" datasetValue=false enableSearchWrapper=false>
    <#assign categoryName=getCategoryName(category)>

    <#if enableSearchWrapper>
        <a href="<@s.url '/search?searchCategory=true&searchText=' + category.getName()/>">
    </#if>

    <div class="category-circle ${classes} <#if settings.getShowCategoriesAsCircles()?? && settings.getShowCategoriesAsCircles() == false>category-square</#if>" style="background-color: ${category.color}" <#if datasetValue>data-value="${category.getID()}"</#if>>
        <span style="color: ${category.getFontColor()}">
            <#if category.getIconReference()??>
                <@header.entityIcon entity=category classes="category-icon"/>
            <#else>
                ${categoryName?capitalize[0]}
            </#if>
        </span>
    </div>

    <#if enableSearchWrapper>
        </a>
    </#if>
</#macro>

<#macro modalIconSelect>
    <div id="modalIconSelect" class="modal modal-fixed-footer background-color">
        <div class="modal-content">


        </div>
        <div class="modal-footer background-color">
            <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
            <@header.buttonLink url='' icon='done' id='button-category-icon-confirm' localizationKey='ok' color='green' classes='modal-action modal-close text-white' noUrl=true disabled=true/>
        </div>
    </div>
</#macro>

