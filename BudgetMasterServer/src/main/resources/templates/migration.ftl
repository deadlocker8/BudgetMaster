<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('title.migration')}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "migration" settings/>
        <#import "helpers/validation.ftl" as validation>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("title.migration")}</div>
                    </div>
                </div>
                <div class="container">
                    <form name="MigrationSettings" action="<@s.url '/migration'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("migration.settings.description")}
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">public</i>
                                <input id="migration-hostname" type="text" name="hostname" <@validation.validation "hostname"/> value="<#if migrationSettings.hostname()??>${migrationSettings.hostname()}</#if>" placeholder="localhost">
                                <label for="migration-hostname">${locale.getString("migration.settings.hostname")}</label>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">dns</i>
                                <input id="migration-port" type="number" min="1" max="65535" name="port" <@validation.validation "port"/> value="<#if migrationSettings.port()??>${migrationSettings.port()?c}</#if>" placeholder="5432">
                                <label for="migration-port">${locale.getString("migration.settings.port")}</label>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">inventory</i>
                                <input id="migration-database-name" type="text" name="port" <@validation.validation "databaseName"/> value="<#if migrationSettings.databaseName()??>${migrationSettings.databaseName()}</#if>">
                                <label for="migration-database-name">${locale.getString("migration.settings.databaseName")}</label>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">person</i>
                                <input id="migration-username" type="text" name="port" <@validation.validation "username"/> value="<#if migrationSettings.username()??>${migrationSettings.username()}</#if>">
                                <label for="migration-username">${locale.getString("migration.settings.username")}</label>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">vpn_key</i>
                                <input id="migration-password" type="text" name="port" <@validation.validation "password"/> value="<#if migrationSettings.password()??>${migrationSettings.password()}</#if>">
                                <label for="migration-password">${locale.getString("migration.settings.password")}</label>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col s12 m12 l8 offset-l2">
                                ${locale.getString("migration.settings.verification.password.description")}
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">lock_open</i>
                                <input id="migration-verification-password" type="password" name="verificationPassword" <@validation.validation "verificationPassword"/> value="">
                                <label for="migration-verification-password">${locale.getString("migration.settings.verification.password")}</label>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col m6 l4 offset-l2 right-align">
                                <@header.buttonLink url='/' icon='clear' localizationKey='cancel' color='red'/>
                            </div>

                            <div class="col m6 l4 left-align">
                                <@header.buttonSubmit name='action' icon='merge' localizationKey='info.button.migration.start' id='buttonMigrate' color='green'/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!-- Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>