<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>
<#import "../transactions/transactionsMacros.ftl" as transactionsMacros>

<#macro searchTextAndButton search>
    <div class="row no-margin-bottom valign-wrapper">
        <div class="col s10 m6 offset-m2 l5 offset-l3">
            <div class="input-field">
                <input id="searchText" type="text" name="searchText" value="${search.getSearchText()}">
                <label for="searchText">${locale.getString("search")}</label>
            </div>
        </div>

        <div class="col s2 m4 l4">
            <div class="hide-on-small-only">
                <@header.buttonSubmit name='action' icon='search' localizationKey='search.submit' id='button-perform-search'/>
            </div>
            <div class="hide-on-med-and-up">
                <@header.buttonSubmit name='action' icon='search' localizationKey='' id='button-perform-search'/>
            </div>
        </div>
    </div>
</#macro>

<#macro checkboxes search>
    <div class="row">
        <div class="col s8 offset-s2 m4 offset-m2 l3 offset-l3">
            <div class="search-checkbox-container">
                <label>
                    <input type="checkbox" name="searchName" <#if search.isSearchName()>checked="checked"</#if>>
                    <span class="text-default">${locale.getString('search.in.name')}</span>
                </label>
            </div>
        </div>
        <div class="col s8 offset-s2 m6 l6">
            <div class="search-checkbox-container">
                <label>
                    <input type="checkbox" name="searchDescription" <#if search.isSearchDescription()>checked="checked"</#if>>
                    <span class="text-default">${locale.getString('search.in.description')}</span>
                </label>
            </div>
        </div>
        <div class="col s8 offset-s2 m4 offset-m2 l3 offset-l3">
            <div class="search-checkbox-container">
                <label>
                    <input type="checkbox" name="searchCategory" <#if search.isSearchCategory()>checked="checked"</#if>>
                    <span class="text-default">${locale.getString('search.in.category')}</span>
                </label>
            </div>
        </div>
        <div class="col s8 offset-s2 m6 l6">
            <div class="search-checkbox-container">
                <label>
                    <input type="checkbox" name="searchTags" <#if search.isSearchTags()>checked="checked"</#if>>
                    <span class="text-default">${locale.getString('search.in.tags')}</span>
                </label>
            </div>
        </div>
        <div class="col s8 offset-s2 m4 offset-m2 l3 offset-l3">
            <div class="search-checkbox-container">
                <label>
                    <input type="checkbox" name="includeHiddenAccounts" <#if search.isIncludeHiddenAccounts()>checked="checked"</#if>>
                    <span class="text-default">${locale.getString('search.include.hidden.accounts')}</span>
                </label>
            </div>
        </div>
    </div>
</#macro>

<#macro dateRange search>
    <div class="row">
        <div class="input-field col s6 m4 offset-m2 l3 offset-l3" id="search-datepicker-container">
            <#if search.getStartDate()??>
                <#assign startDate = dateService.getLongDateString(search.getStartDate())/>
                 <script>
                    startDate = "${startDate}".split(".");
                    startDate = new Date(startDate[2], startDate[1] - 1, startDate[0]);
                 </script>
            <#else>
                <#assign startDate = ''/>
                <script>
                    startDate = null;
                </script>
            </#if>

            <i class="material-icons prefix">today</i>
            <input id="search-datepicker" type="text" class="datepicker" name="startDate" value="${startDate}">
            <label for="search-datepicker">${locale.getString("chart.steps.second.label.start")}</label>
        </div>

        <div class="input-field col s6 m4 l3" id="search-datepicker-end-container">
            <#if search.getEndDate()??>
                <#assign endDate = dateService.getLongDateString(search.getEndDate())/>
                <script>
                    endDate = "${endDate}".split(".");
                    endDate = new Date(endDate[2], endDate[1] - 1, endDate[0]);
                </script>
            <#else>
                <#assign endDate = ''/>
                <script>
                    endDate = null;
                </script>
            </#if>

            <i class="material-icons prefix">event</i>
            <input id="search-datepicker-end" type="text" class="datepicker" name="endDate" value="${endDate}">
            <label for="search-datepicker-end">${locale.getString("chart.steps.second.label.end")}</label>
        </div>
    </div>
</#macro>

<#macro pagination page position>
    <div class="row pagination-position-${position}">
        <div class="col s12 center-align">
            <#if page.getTotalPages() gt 0>
                <ul class="pagination">
                    <li class="text-default <#if page.getNumber() == 0>disabled</#if>"><a class="page-link" data-page="${page.getNumber()-1}"><i class="material-icons">chevron_left</i></a></li>
                        <#list 0..page.getTotalPages()-1 as i>
                            <li class="waves-effect text-default <#if page.getNumber() == i>active</#if>"><a class="page-link" data-page="${i}">${i+1}</a></li>
                        </#list>
                    <li class="text-default <#if page.getNumber() == page.getTotalPages()-1>disabled</#if>"><a class="page-link" data-page="${page.getNumber()+1}"><i class="material-icons">chevron_right</i></a></li>
                </ul>
            </#if>
        </div>
    </div>
</#macro>

<#macro renderTransactions transactions openLinksInNewTab=false>
    <#list transactions as transaction>
        <div class="card-panel search-result">
            <div class="hide-on-large-only">
                <div class="row valign-wrapper">
                    <div class="col s3 center-align bold transaction-text">
                        ${dateService.getDateStringNormal(transaction.date)}
                    </div>
                    <@transactionsMacros.transactionAccountIcon transaction/>
                    <@transactionsMacros.transactionType transaction "s2"/>
                    <@transactionsMacros.transactionLinks transaction=transaction target='_blank'/>
                </div>
                <div class="row valign-wrapper no-margin-bottom">
                    <@transactionsMacros.transactionCategory transaction "center-align"/>
                    <@transactionsMacros.transactionNameAndDescription transaction "s5"/>
                    <@transactionsMacros.transactionAmount transaction transaction.getAccount() "s4"/>
                </div>
            </div>
            <div class="hide-on-med-and-down">
                <div class="row valign-wrapper no-margin-bottom transaction-row-desktop">
                    <div class="col l2 xl1 bold transaction-text transaction-date valign-wrapper">
                        ${dateService.getDateStringNormal(transaction.date)}
                    </div>
                    <@transactionsMacros.transactionCategory transaction "left-align"/>
                    <@transactionsMacros.transactionAccountIcon transaction/>
                    <@transactionsMacros.transactionType transaction "l1 xl1"/>
                    <@transactionsMacros.transactionNameAndDescription transaction "l3 xl4"/>
                    <@transactionsMacros.transactionAmount transaction transaction.getAccount() "l2 xl2"/>
                    <@transactionsMacros.transactionLinks transaction=transaction target='_blank'/>
                </div>
            </div>
        </div>
    </#list>
</#macro>