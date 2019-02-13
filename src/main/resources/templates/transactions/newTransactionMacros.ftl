<#macro isExpenditureSwitch transaction>
    <#assign isPayment = 1>

    <#if transaction.getAmount()?? && (transaction.getAmount() > 0)>
        <#assign isPayment = 0>
        <#assign colorButtonIncome = "budgetmaster-green">
        <#assign colorButtonExpenditure = "budgetmaster-grey budgetmaster-text-isPayment">
    <#else>
        <#assign colorButtonIncome = "budgetmaster-grey budgetmaster-text-isPayment">
        <#assign colorButtonExpenditure = "budgetmaster-red">
    </#if>

    <input type="hidden" name="isPayment" id="input-isPayment" value="${isPayment}">

    <div class="row">
        <div class="col s12 center-align">
            <div class="row hide-on-small-only">
                <div class="col s6 right-align">
                    <@buttonIncome colorButtonIncome/>
                </div>
                <div class="col s6 left-align">
                    <@buttonExpenditure colorButtonExpenditure/>
                </div>
            </div>

            <div class="hide-on-med-and-up">
                <div class="row center-align">
                    <div class="col s12">
                        <@buttonIncome colorButtonIncome/>
                    </div>
                </div>
                <div class="row center-align">
                    <div class="col s12">
                        <@buttonExpenditure colorButtonExpenditure/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonIncome color>
    <a class="waves-effect waves-light btn ${color} buttonIncome"><i class="material-icons left">file_download</i>${locale.getString("title.income")}</a>
</#macro>

<#macro buttonExpenditure color>
    <a class="waves-effect waves-light btn ${color} buttonExpenditure"><i class="material-icons left">file_upload</i>${locale.getString("title.expenditure")}</a>
</#macro>


<#import "../categories/categoriesFunctions.ftl" as categoriesFunctions>
<#macro category categories transaction>
    <div class="row">
        <div class="input-field col s12 m12 l8 offset-l2" id="categoryWrapper">
            <select id="transaction-category" name="category" <@validation.validation "category"/>>
                <#list categories as category>
                    <#assign categoryInfos=categoriesFunctions.getCategoryName(category) + "@@@" + category.getColor() + "@@@" + category.getAppropriateTextColor() + "@@@" + category.getID()?c>

                    <#if category.getType() == "REST">
                        <#continue>
                    </#if>

                    <#if transaction.getCategory()??>
                        <#if transaction.getCategory().getID()?c == category.getID()?c>
                            <option selected value="${category.getID()?c}">${categoryInfos}</option>
                        <#else>
                            <option value="${category.getID()?c}">${categoryInfos}</option>
                        </#if>
                        <#continue>
                    </#if>

                    <#if category.getType() == "NONE">
                        <option selected value="${category.getID()?c}">${categoryInfos}</option>
                        <#continue>
                    </#if>

                    <option value="${category.getID()?c}">${categoryInfos}</option>
                </#list>
            </select>
            <label for="transaction-category">${locale.getString("transaction.new.label.category")}</label>
        </div>
    </div>
</#macro>