<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('settings.database.import')}"/>
        <#import "/spring.ftl" as s>
        <@header.style "collapsible"/>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "settings" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("info.title.database.import.dialog")}</div>
                    </div>
                </div>

                <div class="container">
                    <div class="section center-align">
                        <div class="headline-small">${locale.getString("info.database.import.result")}</div>
                    </div>
                </div>

                <div class="container">
                    <div class="row">
                        <div class="col s10 offset-s1 m8 offset-m2 l6 offset-l3">
                            <table class="bordered">
                                <#list importResultItems as item>
                                    <tr>
                                        <td><i class="material-icons left">${item.getEntityType().getIcon()}</i>
                                            <div class="import-entity-name">${locale.getString(item.getEntityType().getLocalizationKey())}</div>
                                        </td>
                                        <td><#if item.getNumberOfImportedItems() != item.getNumberOfAvailableItems()><i class="fas fa-exclamation-triangle text-red"></i></#if></td>
                                        <td>${item.getNumberOfImportedItems()}/${item.getNumberOfAvailableItems()}</td>
                                    </tr>
                                </#list>
                            </table>
                        </div>
                    </div>
                </div>

                <#if errorMessages?has_content>
                    <div class="container">
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
                                                <#list errorMessages as error>
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

                <div class="container">
                    <div class="row">
                        <div class="col s12 center-align">
                            <@header.buttonLink url='/settings' icon='done' localizationKey='ok' id='button-finish-import'/>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/import.js'/>"></script>
    </@header.body>
</html>