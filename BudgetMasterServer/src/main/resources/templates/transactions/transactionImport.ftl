<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <#import "../helpers/validation.ftl" as validation>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.transactions.import')}"/>
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
                                <div class="col s12 m12 l8 offset-l2 headline-small text-green">
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
            <div class="input-field col s2 offset-s5">
                <input id="separator" type="text" name="separator" <@validation.validation "separator" "center-align"/> value="<#if csvImport??>${csvImport.separator()}</#if>">
                <label class="input-label" for="separator">${locale.getString("transactions.import.separator")}</label>
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

    <div class="container">
        <table class="bordered centered">
            <tr>
                <#list 1..csvRows[0].getColumns()?size as i>
                    <td class="bold">${locale.getString("transactions.import.column")} ${i?c}</td>
                </#list>
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