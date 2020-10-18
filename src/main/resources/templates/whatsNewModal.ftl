<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>

<div id="modalWhatsNew" class="modal background-color">
    <div class="modal-content">
        <div class="row">
            <div class="col s12">
                <h3>${locale.getString("about.version.whatsnew")} in v${build.getVersionName()}</h3>
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <h5>Transaction templates</h5>
                Allows the creation of templates for similar transactions.
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <h5>Auto backup</h5>
                Allows the scheduling of automatic BudgetMaster data backups.
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <h5>Transaction name suggestions</h5>
                Shows suggestions based on the last 25 created transactions.
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <h5>New charts</h5>
                Two new default charts (income/expenditures per year and rest per month).
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <h5>Command line option to set custom directory</h5>
                Specify a folder where settings, database and backups are stored (more details in the wiki).
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <h5>Further information</h5>
                <div>
                    <a href="${locale.getString("roadmap.url")}">${locale.getString("about.roadmap.link")}</a>
                </div>
                <div>
                    More detailed changelog (english only): <a href="https://github.com/deadlocker8/BudgetMaster/releases/tag/v2.4.0">GitHub</a>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-footer background-color">
        <a class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("ok")}</a>
    </div>
</div>

