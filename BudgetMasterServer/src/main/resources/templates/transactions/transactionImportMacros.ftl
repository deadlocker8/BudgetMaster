<#import "../helpers/header.ftl" as header>
<@header.globals/>
<#import "../helpers/validation.ftl" as validation>
<#import "/spring.ftl" as s>

<#import "newTransactionMacros.ftl" as newTransactionMacros>
<#import "../helpers/customSelectMacros.ftl" as customSelectMacros>

<#macro csvUpload>
    <form id="form-csv-import" name="CsvImport" method="POST" action="<@s.url '/transactionImport/upload'/>" enctype="multipart/form-data" accept-charset="UTF-8">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="row">
            <div class="col s12 m12 l8 offset-l2 file-field input-field">
                <div class="btn background-blue">
                    <i class="fas fa-file-csv"></i>
                    <input id="inputCsvImport" type="file" accept=".csv" name="file">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text">
                </div>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s4 l2 offset-l3">
                <input id="separator" type="text" name="separator" <@validation.validation "separator" "center-align"/> value="<#if csvImportSettings.getSeparatorChar()??>${csvImportSettings.getSeparatorChar()}</#if>">
                <label class="input-label" for="separator">${locale.getString("transactions.import.separator")}</label>
            </div>
            <div class="input-field col s4 l2">
                <input id="encoding" type="text" name="encoding" <@validation.validation "encoding" "center-align"/> value="<#if csvImportSettings.getEncoding()??>${csvImportSettings.getEncoding()?upper_case}</#if>">
                <label class="input-label" for="encoding">${locale.getString("transactions.import.encoding")}</label>
            </div>
            <div class="input-field col s4 l2">
                <input id="numberOfLinesToSkip" type="number" name="numberOfLinesToSkip" min="0" name="numberOfLinesToSkip" <@validation.validation "numberOfLinesToSkip" "center-align"/> value="<#if csvImportSettings.getNumberOfLinesToSkip()??>${csvImportSettings.getNumberOfLinesToSkip()?c}</#if>">
                <label class="input-label" for="numberOfLinesToSkip">${locale.getString("transactions.import.numberOfLinesToSkip")}</label>
            </div>
        </div>

        <div class="row">
            <div class="col s12 center-align">
                <@header.buttonSubmit name='action' icon='cloud_upload' localizationKey='settings.database.import' id='button-confirm-csv-import' classes='text-white'/>
            </div>
        </div>
    </form>
</#macro>

<#macro columnSettings>
    <div class="container">
        <form id="form-csv-column-settings" name="CsvColumnSettings" method="POST" action="<@s.url '/transactionImport/columnSettings'/>">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="row">
                <div class="col s6 m4 offset-m2 l3 offset-l3 bold">
                    BudgetMaster
                </div>
                <div class="col s6 m4 l3 bold">
                    CSV
                </div>
            </div>

            <#assign maxColumnCount=csvRows[0].getColumns()?size/>

            <div class="row">
                <div class="col s6 m4 offset-m2 l3 offset-l3">
                    <div class="transaction-import-text-with-icon">
                        <i class="material-icons">event</i>
                        ${locale.getString("transaction.new.label.date")}
                    </div>
                </div>
                <div class="input-field col s6 m6 l4 no-margin-top no-margin-bottom">
                    <input id="columnDate" type="number" min="1" max="${maxColumnCount}" name="columnDate" <@validation.validation "columnDate"/> value="<#if csvImportSettings.getColumnDate()??>${csvImportSettings.getColumnDate()}</#if>">
                    <label class="input-label" for="columnDate">${locale.getString("transactions.import.column")}</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s5 offset-s6 m5 offset-m6 l4 offset-l6 no-margin-top no-margin-bottom">
                    <input id="datePattern" type="text" required name="datePattern" <@validation.validation "datePattern"/> value="<#if csvImportSettings.getDatePattern()??>${csvImportSettings.getDatePattern()}<#else>dd.MM.yyyy</#if>">
                    <label class="input-label" for="datePattern">${locale.getString("transactions.import.datePattern")}</label>
                </div>
                <div class="col s1 m1 l1">
                    <@header.buttonFlat url='https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html' icon='help_outline' localizationKey='' target='_blank' classes='text-default'/>
                </div>
            </div>

            <div class="row">
                <div class="col s6 m4 offset-m2 l3 offset-l3">
                    <div class="transaction-import-text-with-icon">
                        <i class="material-icons">edit</i>
                        ${locale.getString("transaction.new.label.name")}
                    </div>
                </div>
                <div class="input-field col s6 m6 l4 no-margin-top no-margin-bottom">
                    <input id="columnName" type="number" min="1" max="${maxColumnCount}" name="columnName" <@validation.validation "columnName"/> value="<#if csvImportSettings.getColumnName()??>${csvImportSettings.getColumnName()}</#if>">
                    <label class="input-label" for="columnName">${locale.getString("transactions.import.column")}</label>
                </div>
            </div>
            <div class="row">
                <div class="col s6 m4 offset-m2 l3 offset-l3">
                    <div class="transaction-import-text-with-icon">
                        <i class="material-icons">euro</i>
                        ${locale.getString("transaction.new.label.amount")}
                    </div>
                </div>
                <div class="input-field col s6 m6 l4 no-margin-top no-margin-bottom">
                    <input id="columnAmount" type="number" min="1" max="${maxColumnCount}" name="columnAmount" <@validation.validation "columnAmount"/> value="<#if csvImportSettings.getColumnAmount()??>${csvImportSettings.getColumnAmount()}</#if>">
                    <label class="input-label" for="columnAmount">${locale.getString("transactions.import.column")}</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s3 offset-s6 m3 offset-m6 l2 offset-l6 no-margin-top no-margin-bottom">
                    <input id="decimalSeparator" type="text" required maxlength="1" name="decimalSeparator" <@validation.validation "decimalSeparator"/> value="<#if csvImportSettings.getDecimalSeparator()??>${csvImportSettings.getDecimalSeparator()}<#else>${helpers.getDecimalSeparator()}</#if>">
                    <label class="input-label" for="decimalSeparator">${locale.getString("transactions.import.decimalSeparator")}</label>
                </div>

                <div class="input-field col s3 m3 l2 no-margin-top no-margin-bottom">
                    <input id="groupingSeparator" type="text" required maxlength="1" name="groupingSeparator" <@validation.validation "groupingSeparator"/> value="<#if csvImportSettings.getGroupingSeparator()??>${csvImportSettings.getGroupingSeparator()}<#else>${helpers.getGroupingSeparator()}</#if>">
                    <label class="input-label" for="groupingSeparator">${locale.getString("transactions.import.groupingSeparator")}</label>
                </div>
            </div>

            <div class="row">
                <div class="col s6 m4 offset-m2 l3 offset-l3">
                    <div class="transaction-import-text-with-icon">
                        <i class="material-icons">article</i>
                        ${locale.getString("transaction.new.label.description")}
                    </div>
                </div>
                <div class="input-field col s6 m6 l4 no-margin-top no-margin-bottom">
                    <input id="columnDescription" type="number" min="1" max="${maxColumnCount}" name="columnDescription" <@validation.validation "columnDescription"/> value="<#if csvImportSettings.getColumnDescription()??>${csvImportSettings.getColumnDescription()}</#if>">
                    <label class="input-label" for="columnDescription">${locale.getString("transactions.import.column")}</label>
                </div>
            </div>

            <br>

            <div class="row">
                <div class="col s12 center-align">
                    <@header.buttonSubmit name='action' icon='save' localizationKey='save' id='button-confirm-csv-column-settings' classes='text-white'/>
                </div>
            </div>
        </form>
    </div>
</#macro>

<#macro renderCsvRows>
    <div id="transaction-import-overview">
        <table class="bordered centered">
            <tr>
                <#if csvRows?has_content>
                    <#assign numberOfColumns=csvRows[0].getColumns()?size/>
                    <#list 1..numberOfColumns as i>
                        <td class="bold">${locale.getString("transactions.import.column")} ${i?c}</td>
                    </#list>
                </#if>
            </tr>

            <#list csvRows as cswRow>
                <tr>
                    <#list cswRow.getColumns() as csvColumn>
                        <td>${csvColumn}</td>
                    </#list>
                </tr>
            </#list>
        </table>
    </div>
</#macro>

<#macro renderCsvTransactions>
    <div id="transaction-import-list">
        <table class="centered" id="table-transaction-rows" style="width:100%">
            <thead>
                <tr>
                    <td class="bold">${locale.getString("transactions.import.status")}</td>
                    <td class="bold">${locale.getString("transaction.new.label.date")}</td>
                    <td class="bold">${locale.getString("transaction.new.label.category")}</td>
                    <td class="bold">${locale.getString("transaction.new.label.name")}</td>
                    <td class="bold">${locale.getString("transaction.new.label.description")}</td>
                    <td class="bold">${locale.getString("transaction.new.label.amount")}</td>
                    <td class="bold">${locale.getString("transactions.import.actions")}</td>
                </tr>
            </thead>

            <tbody>
                <#list csvTransactions as csvTransaction>
                    <@renderCsvTransaction csvTransaction csvTransaction?index/>
                </#list>
            </tbody>
        </table>

        <div class="hidden">
            <#list csvTransactions as csvTransaction>
                <form name="NewTransactionInPlace" id="newTransactionInPlace_${csvTransaction?index}" method="POST" action="<@s.url '/transactionImport/' + csvTransaction?index + '/newTransactionInPlace'/>" data-index="${csvTransaction?index}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </#list>
        </div>
    </div>

    <@newTransactionMacros.insertNameSuggestions/>
</#macro>

<#macro renderCsvTransaction csvTransaction index>
    <tr class="transaction-import-row <#if csvTransaction.getStatus().name() == 'SKIPPED'>transaction-import-row-skipped</#if>" id="transaction-import-row-${index}">
            <td data-order="${locale.getString(csvTransaction.getStatus().getLocalizationKey())}" data-search="${locale.getString(csvTransaction.getStatus().getLocalizationKey())}"><@statusBanner csvTransaction.getStatus()/></td>
            <td data-order="${csvTransaction.getDate()}" data-search="${csvTransaction.getDate()}">${csvTransaction.getDate()}</td>
            <td data-order="${csvTransaction.getCategory().getName()}" data-search="${csvTransaction.getCategory().getName()}">
                <@customSelectMacros.customCategorySelect categories csvTransaction.getCategory() "left-align no-margin-top no-margin-bottom" "" "csvTransaction-category-${index}" false "no-margin-bottom" csvTransaction.getStatus().name() == 'SKIPPED' 'newTransactionInPlace_${index}'/>
            </td>
            <td data-order="${csvTransaction.getName()}" data-search="${csvTransaction.getName()}">
                <div class="input-field no-margin-top no-margin-bottom">
                    <input form="newTransactionInPlace_${index}" class="no-margin-bottom autocomplete" type="text" name="name" autocomplete="off" required value="${csvTransaction.getName()}" <#if csvTransaction.getStatus().name() == 'SKIPPED'>disabled</#if>>
                </div>
            </td>
            <td data-order="${csvTransaction.getDescription()}" data-search="${csvTransaction.getDescription()}">
                <div class="input-field no-margin-top no-margin-bottom">
                    <input form="newTransactionInPlace_${index}" class="no-margin-bottom" type="text" name="description" value="${csvTransaction.getDescription()}" <#if csvTransaction.getStatus().name() == 'SKIPPED'>disabled</#if>>
                </div>
            </td>
            <td data-order="${currencyService.getCurrencyString(csvTransaction.getAmount())}" data-search="${currencyService.getCurrencyString(csvTransaction.getAmount())}">${currencyService.getCurrencyString(csvTransaction.getAmount())}</td>
            <td>
                <#if csvTransaction.getStatus().name() == 'SKIPPED'>
                    <@header.buttonFlat url='/transactionImport/' + index + '/undoSkip' isDataUrl=true icon='do_disturb_off' localizationKey='' classes="no-padding text-default button-request-transaction-import-undo-skip" datasetIndex=index/>
                <#else>
                    <@header.buttonSubmit name='action' icon='save' localizationKey='' classes='text-white' form='newTransactionInPlace_${index}'/>&nbsp;
                    <div class="fixed-action-btn edit-transaction-button">
                        <a class="btn-floating btn-flat waves-effect waves-light no-padding text-default edit-transaction-button-link">
                            <i class="material-icons text-default">edit</i>
                        </a>
                        <ul class="new-transaction-button-list">
                            <li>
                                <a href="<@s.url '/transactionImport/' + index + '/newTransaction/normal'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new", locale.getString("title.transaction.new.normal"))}</a>
                                <a href="<@s.url '/transactionImport/' + index + '/newTransaction/normal'/>" class="btn-floating btn background-orange"><i class="material-icons">payment</i></a>
                            </li>
                            <li>
                                <a href="<@s.url '/transactionImport/' + index + '/newTransaction/transfer'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new", locale.getString("title.transaction.new.transfer"))}</a>
                                <a href="<@s.url '/transactionImport/' + index + '/newTransaction/transfer'/>" class="btn-floating btn background-green-dark"><i class="material-icons">swap_horiz</i></a>
                            </li>
                            <li>
                                <a href="<@s.url '/transactionImport/' + index + '/newFromTemplate'/>" class="btn-floating btn mobile-fab-tip no-wrap">${locale.getString("title.transaction.new", locale.getString("title.transaction.new.from.template"))}</a>
                                <a href="<@s.url '/transactionImport/' + index + '/newFromTemplate'/>" class="btn-floating btn background-blue-baby"><i class="material-icons">file_copy</i></a>
                            </li>
                        </ul>
                    </div>
                    <@header.buttonFlat url='/transactionImport/' + index + '/skip' isDataUrl=true icon='do_not_disturb_on' localizationKey='' classes="no-padding text-default button-request-transaction-import-skip" datasetIndex=index/>
                </#if>
            </td>
        </form>
    </tr>
</#macro>

<#macro showColumnSettingsErrors>
    <#if errorsColumnSettings?has_content>
        <div class="container" id="parseErrors">
            <div class="row">
                <div class="col s12">
                    <ul class="collapsible">
                        <li>
                            <div class="collapsible-header bold">
                                <i class="fas fa-exclamation-triangle text-red"></i>
                                ${locale.getString("info.database.import.result.errors")}
                            </div>
                            <div class="collapsible-body">
                                <table class="bordered">
                                    <#list errorsColumnSettings as error>
                                        <tr>
                                            <td><i class="fas fa-exclamation-triangle text-red"></i></td>
                                            <td>${error}</td>
                                        </tr>
                                    </#list>
                                </table>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </#if>
</#macro>

<#macro statusBanner status>
    <#assign bannerText=locale.getString(status.getLocalizationKey())>
    <#if status.name() == "PENDING">
        <#assign bannerClasses="background-blue text-white">
    <#elseif status.name() == "IMPORTED">
        <#assign bannerClasses="background-green text-white">
    <#elseif status.name() == "SKIPPED">
        <#if settings.isUseDarkTheme()>
            <#assign bannerClasses="background-grey text-black">
        <#else>
            <#assign bannerClasses="background-grey text-white">
        </#if>
    </#if>

    <div class="banner ${bannerClasses}">${bannerText}</div>
</#macro>