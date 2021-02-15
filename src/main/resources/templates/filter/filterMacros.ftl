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

<#macro buttonsCharts>
    <div class="row hide-on-small-only valign-wrapper">
        <div class="col s6 right-align">
            <@buttonResetChart/>
        </div>

        <div class="col s6 left-align">
            <@buttonClose/>
        </div>
    </div>

    <div class="hide-on-med-and-up valign-wrapper">
        <div class="row center-align">
            <div class="col s12">
                <@buttonResetChart/>
            </div>
        </div>
        <div class="row center-align">
            <div class="col s12">
                <@buttonClose/>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonReset>
    <a href="<@s.url '/filter/reset'/>" class="waves-effect waves-light btn background-blue"><i class="material-icons left">settings_backup_restore</i>${locale.getString("filter.reset")}</a>
</#macro>

<#macro buttonApply>
    <button class="btn waves-effect waves-light background-blue" type="submit" name="buttonSave">
        <i class="fas fa-filter left"></i>${locale.getString("filter.apply")}
    </button>
</#macro>

<#macro buttonResetChart>
    <a class="filter-button-reset waves-effect waves-light btn background-blue"><i class="material-icons left">settings_backup_restore</i>${locale.getString("filter.reset")}</a>
</#macro>

<#macro buttonClose>
    <a class="filter-button-close waves-effect waves-light background-blue btn white-text"><i class="fas fa-filter left"></i>${locale.getString("filter.apply")}</a>
</#macro>

<#macro buttonsAllOrNone>
    <div class="row no-margin">
        <div class="col s6 right-align">
            <a class="waves-effect waves-light btn background-blue filter-button-all">${locale.getString("filter.tags.button.all")}</a>
        </div>
        <div class="col s6">
            <a class="waves-effect waves-light btn background-blue filter-button-none">${locale.getString("filter.tags.button.none")}</a>
        </div>
    </div>
</#macro>

<#macro filterModal filterConfiguration>
    <div id="modalFilter" class="modal background-color">
        <div class="modal-content">
            <h4>${locale.getString("title.filter")}</h4>
            <form name="NewFilterConfiguration" action="<@s.url '/filter/apply'/>" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <@filterModalContent filterConfiguration/>
                <@buttons/>
            </form>
        </div>
        <div class="modal-footer background-color">
            <a href="" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
        </div>
    </div>
</#macro>

<#macro filterModalCharts filterConfiguration>
    <div id="modalFilter" class="modal background-color">
        <div class="modal-content">
            <h4>${locale.getString("title.filter")}</h4>
            <@filterModalContent filterConfiguration "filterConfiguration"/>
            <@buttonsCharts/>
        </div>
        <div class="modal-footer background-color">
            <a class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
        </div>
    </div>
</#macro>

<#macro filterModalContent filterConfiguration prefix="">
    <#if prefix?length gt 0>
        <#assign prefixValue = prefix + "."/>
    <#else>
        <#assign prefixValue = ""/>
    </#if>
    <div class="row">
        <div class="col s12">
            <ul class="collapsible z-depth-2" id="filterSettings">
                <li id="section-type">
                    <div class="collapsible-header">
                        <i class="fas fa-piggy-bank"></i>
                        ${locale.getString("filter.type")}
                        <div class="collapsible-header-status"></div>
                    </div>
                    <div class="collapsible-body">
                        <div class="row no-margin">
                            <div class="col s12 m4 l4">
                                <label>
                                    <input type="checkbox" name="${prefixValue}includeIncome" <#if filterConfiguration.isIncludeIncome()>checked="checked"</#if>>
                                    <span class="text-color">${locale.getString('filter.type.income')}</span>
                                </label>
                            </div>
                            <div class="col s12 m4 l4">
                                <label>
                                    <input type="checkbox" name="${prefixValue}includeExpenditure" <#if filterConfiguration.isIncludeExpenditure()>checked="checked"</#if>>
                                    <span class="text-color">${locale.getString('filter.type.expenditure')}</span>
                                </label>
                            </div>
                            <div class="col s12 m4 l4">
                                <label>
                                    <input type="checkbox" name="${prefixValue}includeTransfer" <#if filterConfiguration.isIncludeTransfer()>checked="checked"</#if>>
                                    <span class="text-color">${locale.getString('filter.type.transfer')}</span>
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
                                    <input type="checkbox" name="${prefixValue}includeNotRepeating" <#if filterConfiguration.isIncludeNotRepeating()>checked="checked"</#if>>
                                    <span class="text-color">${locale.getString('filter.repeating.false')}</span>
                                </label>
                            </div>
                            <div class="col s6 m6 l6">
                                <label>
                                    <input type="checkbox" name="${prefixValue}includeRepeating" <#if filterConfiguration.isIncludeRepeating()>checked="checked"</#if>>
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
                        <@buttonsAllOrNone/>
                        <div class="row no-margin">
                            <div class="col s12">
                                <#list filterConfiguration.getFilterCategories() as filterCategory>
                                    <div>
                                        <label>
                                            <input type="checkbox" name="${prefixValue}filterCategories['${filterCategory?index}'].include" <#if filterCategory.isInclude()>checked="checked"</#if>>
                                            <span class="text-color">${filterCategory.getName()}</span>
                                        </label>
                                        <input type="hidden" name="${prefixValue}filterCategories['${filterCategory?index}'].ID" value="${filterCategory.getID()}"/>
                                        <input type="hidden" name="${prefixValue}filterCategories['${filterCategory?index}'].name" value="${filterCategory.getName()}"/>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </li>

                <li id="section-tags">
                    <div class="collapsible-header">
                        <i class="material-icons">local_offer</i>
                        ${locale.getString("filter.tags")}
                        <div class="collapsible-header-status"></div>
                    </div>
                    <div class="collapsible-body">
                        <@buttonsAllOrNone/>
                        <div class="row no-margin">
                            <div class="col s12">
                                <#list filterConfiguration.getFilterTags() as filterTag>
                                    <div>
                                        <label>
                                            <input type="checkbox" name="${prefixValue}filterTags['${filterTag?index}'].include" <#if filterTag.isInclude()>checked="checked"</#if>>
                                            <span class="text-color">${filterTag.getName()}</span>
                                        </label>
                                        <input type="hidden" name="${prefixValue}filterTags['${filterTag?index}'].ID" value="${filterTag.getID()}"/>
                                        <input type="hidden" name="${prefixValue}filterTags['${filterTag?index}'].name" value="${filterTag.getName()}"/>
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
                                <input id="filter-name" type="text" name="${prefixValue}name" value="<#if filterConfiguration.getName()??>${filterConfiguration.getName()}</#if>">
                                <label for="filter-name">${locale.getString("filter.name.contains")}</label>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</#macro>
