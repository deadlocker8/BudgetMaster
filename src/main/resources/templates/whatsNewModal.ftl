<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "helpers/header.ftl" as header>

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
                    ${locale.getString("news.all.releases")} <a target="_blank" href="${locale.getString("roadmap.url")}">${locale.getString("about.roadmap.link")}</a>
                </div>
                <div>
                    ${locale.getString("news.detailed")} <a target="_blank" href="https://github.com/deadlocker8/BudgetMaster/releases/tag/v${build.getVersionName()}">GitHub</a>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-footer background-color">
        <@header.buttonLink url='/about/whatsNewModal/close' icon='done' localizationKey='ok' color='green' id='buttonCloseWhatsNew' classes='modal-action modal-close text-white'/>
    </div>
</div>

