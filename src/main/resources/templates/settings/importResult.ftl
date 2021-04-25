<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('settings.database.import')}"/>
        <#import "/spring.ftl" as s>
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
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="row">
                            <div class="col s10 offset-s1 m8 offset-m2 l6 offset-l3">
                                <table>
                                    <#list numberOfImportedEntitiesByType as entityType, numberOfItems>
                                        <tr>
                                            <td><i class="material-icons left">${entityType.getIcon()}</i>
                                                <div class="import-entity-name">${locale.getString(entityType.getLocalizationKey())}</div>
                                            </td>
                                            <td>${numberOfItems}</td>
                                        </tr>
                                    </#list>
                                </table>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col m6 l4 offset-l2 right-align">
                                <@header.buttonLink url='/settings' icon='done' localizationKey='ok'/>
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