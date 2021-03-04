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

<#macro categoryCircle category classes="">
    <#assign categoryName=getCategoryName(category)>

    <div class="category-circle ${classes}" style="background-color: ${category.color}">
        <span style="color: ${category.getAppropriateTextColor()}">
            <#if category.getIcon()??>
                <i class="${category.getIcon()}"></i>
            <#else>
                ${categoryName?capitalize[0]}
            </#if>
        </span>
    </div>
</#macro>