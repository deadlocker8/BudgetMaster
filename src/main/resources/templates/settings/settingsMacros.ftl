<#import "/spring.ftl" as s>

<#macro switches settings>
    <div class="row">
        <div class="col s12">
            <div class="table-container">
                <div class="table-cell">
                    <div class="switch-cell-margin">${locale.getString("settings.rest")}</div>
                    <div class="switch-cell-margin">${locale.getString("settings.darkTheme")}</div>
                    <div class="switch-cell-margin">${locale.getString("settings.updates.automatic")}</div>
                </div>
                <div class="table-cell table-cell-spacer"></div>
                <div class="table-cell">
                    <@switch "rest" "restActivated" settings.isRestActivated()/>
                    <@switch "darkTheme" "useDarkTheme" settings.isUseDarkTheme()/>
                    <@switch "updates.automatic" "autoUpdateCheckEnabled" settings.isAutoUpdateCheckEnabled()/>
                </div>
                <div class="table-cell table-cell-spacer"></div>
                <div class="table-cell">
                    <div class="switch-cell-margin">
                        <a class="btn btn-flat tooltipped text-color" data-position="bottom" data-tooltip="${locale.getString("settings.rest.description")}"><i class="material-icons">help_outline</i></a>
                    </div>
                    <div class="switch-cell-margin">
                        <a class="btn btn-flat tooltipped text-color" data-position="bottom" data-tooltip="${locale.getString("settings.darkTheme.description")}"><i class="material-icons">help_outline</i></a>
                    </div>
                    <div class="switch-cell-margin">
                        <a class="btn btn-flat tooltipped text-color" data-position="bottom" data-tooltip="${locale.getString("settings.updates.automatic.description")}"><i class="material-icons">help_outline</i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro switch localizationKey name isActive>
    <div class="switch switch-cell-margin">
        <label>
            ${locale.getString("settings.${localizationKey}.deactivated")}
            <input type="checkbox" name="${name}" <#if isActive>checked</#if>/>
            <span class="lever"></span>
            ${locale.getString("settings.${localizationKey}.activated")}
        </label>
    </div>
</#macro>

<#macro databaseNormal>
    <div class="row hide-on-small-only">
        <div class="col m4 l4 center-align">
            <a href="<@s.url '/settings/database/requestImport'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_upload</i>${locale.getString("settings.database.import")}</a>
        </div>

        <div class="col m4 l4 center-align">
            <a href="<@s.url '/settings/database/requestExport'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_download</i>${locale.getString("settings.database.export")}</a>
        </div>

        <div class="col m4 l4 center-align">
            <a href="<@s.url '/settings/database/requestDelete'/>" class="waves-effect waves-light btn budgetmaster-red"><i class="material-icons left">delete_forever</i>${locale.getString("settings.database.delete")}</a>
        </div>
    </div>
</#macro>

<#macro databaseSmall>
    <div class="hide-on-med-and-up">
        <div class="row center-align">
            <div class="col s12">
                <a href="<@s.url '/settings/database/requestImport'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_upload</i>${locale.getString("settings.database.import")}</a>
            </div>
        </div>

        <div class="row center-align">
            <div class="col s12">
                <a href="<@s.url '/settings/database/requestExport'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">cloud_download</i>${locale.getString("settings.database.export")}</a>
            </div>
        </div>

        <div class="row center-align">
            <div class="col s12">
                <a href="<@s.url '/settings/database/requestDelete'/>" class="waves-effect waves-light btn budgetmaster-red"><i class="material-icons left">delete_forever</i>${locale.getString("settings.database.delete")}</a>
            </div>
        </div>
    </div>
</#macro>

<#macro deleteDB verificationCode>
    <div id="modalConfirmDelete" class="modal background-color">
        <div class="modal-content">
            <h4>${locale.getString("info.title.database.delete")}</h4>
            <p>${locale.getString("info.header.text.database.delete")}</p>
            <p>${locale.getString("info.text.database.delete", verificationCode)}</p>

            <form id="form-confirm-database-delete" action="<@s.url '/settings/database/delete'/>" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="verificationCode" value="${verificationCode}"/>

                <div class="row">
                    <div class="input-field col s12 m8 l6">
                        <input id="verification" type="text" name="verificationUserInput">
                        <label for="verification">${locale.getString("settings.database.delete.verification")}</label>
                    </div>
                </div>
            </form>
        </div>
        <div class="modal-footer background-color">
            <a href="<@s.url '/settings'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
            <a class="modal-action modal-close waves-effect waves-light green btn-flat white-text" id="button-confirm-database-delete">${locale.getString("delete")}</a>
        </div>
    </div>
</#macro>

<#macro importDB>
    <div id="modalImportDatabase" class="modal background-color">
        <div class="modal-content">
            <h4>${locale.getString("info.title.database.import.dialog")}</h4>

            <form id="form-database-import" method="POST" action="<@s.url '/settings/database/upload'/>" enctype="multipart/form-data" accept-charset="UTF-8">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="file-field input-field">
                    <div class="btn budgetmaster-blue">
                        <i class="material-icons">cloud_upload</i>
                        <input id="inputDatabaseImport" type="file" accept=".json" name="file">
                    </div>
                    <div class="file-path-wrapper">
                        <input class="file-path validate" type="text">
                    </div>
                </div>
            </form>
        </div>
        <div class="modal-footer background-color">
            <a href="<@s.url '/settings'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
            <a class="modal-action modal-close waves-effect waves-light green btn-flat white-text" id="button-confirm-database-import">${locale.getString("settings.database.import")}</a>
        </div>
    </div>
</#macro>

<#macro errorImport error>
    <div id="modalErrorImportDatabase" class="modal background-color">
        <div class="modal-content">
            <h4>${locale.getString("error.title.database.import")}</h4>
            <p>${locale.getString("error.text.database.import", error)}</p>
        </div>
        <div class="modal-footer background-color">
            <a href="<@s.url '/settings'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("ok")}</a>
        </div>
    </div>
</#macro>

<#macro update>
    <div id="modelPerformUpdate" class="modal background-color">
        <div class="modal-content">
            <h4>${locale.getString("info.title.update")}</h4>
            <p>${updateString}</p>
        </div>
        <div class="modal-footer background-color">
            <a href="<@s.url '/settings'/>" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
            <a href="<@s.url '/settings/performUpdate'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("settings.update.start")}</a>
        </div>
    </div>
</#macro>