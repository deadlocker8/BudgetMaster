<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>

<div id="modalWhatsNew" class="modal modal-fixed-footer background-color">
    <div class="modal-content">
        <div class="row">
            <div class="col s12">
                <h3>${locale.getString("about.version.whatsnew")} in v${build.getVersionName()}</h3>
            </div>
        </div>

        <#list newsEntries as entry>
            <div class="row">
                <div class="col s12">
                    <h5>${entry.getHeadline()}</h5>
                    ${entry.getDescription()}
                </div>
            </div>
        </#list>

        <div class="row">
            <div class="col s12">
                <h5>${locale.getString("news.further.information")}</h5>
                <div>
                    ${locale.getString("about.date")} ${build.getVersionDate()}
                </div>
                <div>
                    ${locale.getString("news.all.releases")} <a href="${locale.getString("roadmap.url")}">${locale.getString("about.roadmap.link")}</a>
                </div>
                <div>
                    ${locale.getString("news.detailed")} <a href="https://github.com/deadlocker8/BudgetMaster/releases/tag/v${build.getVersionName()}">GitHub</a>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-footer background-color">
        <a id="buttonCloseWhatsNew" href="<@s.url '/about/whatsNewModal/close'/>" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("ok")}</a>
    </div>
</div>

