<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <#import "../helpers/validation.ftl" as validation>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.transactions.import')}"/>
        <@header.style "transactionImport"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "importCSV" settings/>

        <#import "../search/searchMacros.ftl" as searchMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("menu.transactions.import")}</div>
                    </div>
                </div>

                <@header.content>
                    <div class="container">
                        <#if !error?? && csvImport.getFileName()??>
                            <div class="row center-align">
                                <div class="col s12 m12 l8 offset-l2 headline-small text-green truncate">
                                    <i class="fas fa-file-csv"></i> ${csvImport.getFileName()}
                                </div>
                            </div>

                            <div class="row center-align">
                                <div class="col s12">
                                    <@header.buttonLink url='/transactionImport/cancel' icon='clear' localizationKey='cancel' color='red' classes='text-white'/>
                                </div>
                            </div>
                        <#else>
                            <@csvUpload/>
                        </#if>
                    </div>

                    <#if csvRows??>
                        <@renderCsvRows/>
                    </#if>
                </@header.content>
            </div>
        </main>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>

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
                <input id="separator" type="text" name="separator" <@validation.validation "separator" "center-align"/> value="<#if csvImport??>${csvImport.separator()}</#if>">
                <label class="input-label" for="separator">${locale.getString("transactions.import.separator")}</label>
            </div>
            <div class="input-field col s4 l2">
                <input id="encoding" type="text" name="encoding" <@validation.validation "encoding" "center-align"/> value="<#if csvImport??>${csvImport.encoding()?upper_case}</#if>">
                <label class="input-label" for="encoding">${locale.getString("transactions.import.encoding")}</label>
            </div>
            <div class="input-field col s4 l2">
                <input id="numberOfLinesToSkip" type="number" name="numberOfLinesToSkip" min="0" name="numberOfLinesToSkip" <@validation.validation "numberOfLinesToSkip" "center-align"/> value="<#if csvImport??>${csvImport.numberOfLinesToSkip()?c}</#if>">
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

<#macro renderCsvRows>
    <div class="container">
        <div class="section center-align">
            <div class="headline-small">${locale.getString("transactions.import.overview")}</div>
        </div>
    </div>

    <div class="container" id="transaction-import-overview">
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