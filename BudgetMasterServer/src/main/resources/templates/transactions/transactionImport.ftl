<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <#import "../helpers/validation.ftl" as validation>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.transactions.import')}"/>
        <@header.style "transactions"/>
        <@header.style "transactionImport"/>
        <@header.style "collapsible"/>
        <#import "/spring.ftl" as s>
        <link rel="stylesheet" href="<@s.url '/webjars/datatables/1.13.2/css/jquery.dataTables.min.css'/>"/>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "importCSV" settings/>

        <#import "transactionsMacros.ftl" as transactionMacros>
        <#import "newTransactionMacros.ftl" as newTransactionMacros>
        <#import "transactionImportMacros.ftl" as transactionImportMacros>
        <#import "../helpers/customSelectMacros.ftl" as customSelectMacros>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="fas fa-file-csv"></i> ${locale.getString("menu.transactions.import")}</div>
                    </div>
                </div>

                <@header.content>
                    <div class="container">
                        <#if csvRows??>
                            <div class="row center-align">
                                <div class="col s12 m12 l8 offset-l2 headline-small text-green truncate" id="csv-file-name">
                                    <i class="fas fa-file-csv"></i> ${csvImport.getFileName()}
                                </div>
                            </div>

                            <div class="row center-align">
                                <div class="col s12">
                                    <@header.buttonLink url='/transactionImport/cancel' icon='clear' localizationKey='cancel' id='button-cancel-csv-import' color='red' classes='text-white'/>
                                </div>
                            </div>
                        <#else>
                            <@transactionImportMacros.csvUpload/>
                        </#if>
                    </div>

                    <#if csvTransactions??>
                        <@transactionImportMacros.renderCsvTransactions/>
                        <@transactionImportMacros.showColumnSettingsErrors />
                    <#elseif csvRows?? >
                        <div class="container">
                            <div class="section center-align">
                                <div class="headline-small">${locale.getString("transactions.import.matchColumns")}</div>
                            </div>
                        </div>
                        <@transactionImportMacros.columnSettings/>
                        <@transactionImportMacros.renderCsvRows/>
                    </#if>
                </@header.content>
            </div>
        </main>

        <script>
            localizedSearch = '${locale.getString("search")}';
        </script>

        <!--  Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/webjars/datatables/1.13.2/js/jquery.dataTables.min.js'/>"></script>
        <script src="<@s.url '/js/transactionImport.js'/>"></script>
    </@header.body>
</html>
