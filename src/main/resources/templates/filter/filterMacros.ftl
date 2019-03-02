<#import "/spring.ftl" as s>

<#macro buttons>
    <div class="row hide-on-small-only valign-wrapper">
        <div class="col s6 right-align">
            <@buttonReset/>
        </div>

        <div class="col s6 left-align">
            <@buttonApply/>
        </div>
    </div>

    <div class="hide-on-med-and-up valign-wrapper">
        <div class="row center-align">
            <div class="col s12">
                <@buttonReset/>
            </div>
        </div>
        <div class="row center-align">
            <div class="col s12">
                <@buttonApply/>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonReset>
    <a href="<@s.url '/filter/reset'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">settings_backup_restore</i>${locale.getString("filter.reset")}</a>
</#macro>

<#macro buttonApply>
    <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave">
        <i class="fas fa-filter left"></i>${locale.getString("filter.apply")}
    </button>
</#macro>

<#macro filterModal filterConfiguration>
    <div id="modalFilter" class="modal background-color">
        <div class="modal-content">
            <h4>${locale.getString("title.filter")}</h4>
            <form name="NewFilterConfiguration" action="<@s.url '/filter/apply'/>" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="row">
                    <div class="s12">
                        <ul class="collapsible z-depth-2" id="filterSettings">
                            <li id="section-type">
                                <div class="collapsible-header">
                                    <i class="fas fa-piggy-bank"></i>
                                    ${locale.getString("filter.type")}
                                    <div class="collapsible-header-status"></div>
                                </div>
                                <div class="collapsible-body">
                                    <div class="row no-margin">
                                        <div class="col s6 m6 l6">
                                            <label>
                                                <input type="checkbox" name="includeIncome" <#if filterConfiguration.isIncludeIncome()>checked="checked"</#if>>
                                                <span class="text-color">${locale.getString('filter.type.income')}</span>
                                            </label>
                                        </div>
                                        <div class="col s6 m6 l6">
                                            <label>
                                                <input type="checkbox" name="includeExpenditure" <#if filterConfiguration.isIncludeExpenditure()>checked="checked"</#if>>
                                                <span class="text-color">${locale.getString('filter.type.expenditure')}</span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </li>

                            <li id="section-repeating">
                                <div class="collapsible-header">
                                    <i class="material-icons">repeat</i>
                                    ${locale.getString("filter.repeating")}
                                    <div class="collapsible-header-status"></div>
                                </div>
                                <div class="collapsible-body">
                                    <div class="row no-margin">
                                        <div class="col s6 m6 l6">
                                            <label>
                                                <input type="checkbox" name="includeNotRepeating" <#if filterConfiguration.isIncludeNotRepeating()>checked="checked"</#if>>
                                                <span class="text-color">${locale.getString('filter.repeating.false')}</span>
                                            </label>
                                        </div>
                                        <div class="col s6 m6 l6">
                                            <label>
                                                <input type="checkbox" name="includeRepeating" <#if filterConfiguration.isIncludeRepeating()>checked="checked"</#if>>
                                                <span class="text-color">${locale.getString('filter.repeating.true')}</span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </li>

                            <li id="section-categories">
                                <div class="collapsible-header">
                                    <i class="material-icons">label</i>
                                    ${locale.getString("filter.categories")}
                                    <div class="collapsible-header-status"></div>
                                </div>
                                <div class="collapsible-body">
                                    <div class="row no-margin">
                                        <div class="col s12">
                                            <#list filterConfiguration.getFilterCategories() as filterCategory>
                                                <div>
                                                    <label>
                                                        <input type="checkbox" name="filterCategories['${filterCategory?index}'].include" <#if filterCategory.isInclude()>checked="checked"</#if>>
                                                        <span class="text-color">${filterCategory.getName()}</span>
                                                    </label>
                                                    <input type="hidden" name="filterCategories['${filterCategory?index}'].ID" value="${filterCategory.getID()}"/>
                                                    <input type="hidden" name="filterCategories['${filterCategory?index}'].name" value="${filterCategory.getName()}"/>
                                                </div>
                                            </#list>
                                        </div>
                                    </div>
                                </div>
                            </li>

                            <li id="section-name">
                                <div class="collapsible-header">
                                    <i class="material-icons">subject</i>
                                    ${locale.getString("filter.name")}
                                    <div class="collapsible-header-status"></div>
                                </div>
                                <div class="collapsible-body">
                                    <div class="row no-margin">
                                        <div class="input-field col s12">
                                            <input id="filter-name" type="text" name="name" value="<#if filterConfiguration.getName()??>${filterConfiguration.getName()}</#if>">
                                            <label for="filter-name">${locale.getString("filter.name.contains")}</label>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>

                <@buttons/>
            </form>
        </div>
        <div class="modal-footer background-color">
            <a href="" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
        </div>
    </div>
</#macro>
