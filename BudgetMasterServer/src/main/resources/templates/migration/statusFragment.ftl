<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<div class="row">
    <div class="col s12 m12 l8 offset-l2">
        <div class="headline-small migration-status"><i class="${status.getIcon()} ${status.getTextColor()} migration-status-icon"></i> ${locale.getString(status.getLocalizationKey())}</div>
    </div>
</div>

<#if status.name() == "RUNNING">
    <div class="row">
        <div class="col s12 m12 l8 offset-l2">
            <div class="preloader-wrapper small active" id="progress-spinner">
                <div class="spinner-layer spinner-blue-only">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div>
                    <div class="gap-patch">
                        <div class="circle"></div>
                    </div>
                    <div class="circle-clipper right">
                        <div class="circle"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#if>

<#if status.name() == "SUCCESS">
    <div class="row">
        <div class="col s12">
            <ul class="collapsible">
                <li>
                    <div class="collapsible-header bold">
                        <i class="fas fa-info"></i>
                        ${locale.getString("migration.status.summary")}
                    </div>
                    <div class="collapsible-body">
                        <table class="bordered">
                            <#list summary as summaryLine>
                                <tr>
                                    <td>${summaryLine}</td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </li>
            </ul>
        </div>
    </div>

    <div class="row" id="button-migration-home">
        <div class="col s12 m12 l8 offset-l2">
            <@header.buttonLink url='/' icon='home' localizationKey='menu.home'/>
        </div>
    </div>
</#if>

<#if status.name() == "ERROR">
    <div class="row">
        <div class="col s12">
            <ul class="collapsible">
                <li>
                    <div class="collapsible-header bold">
                        <i class="${status.getIcon()} ${status.getTextColor()}"></i>
                        ${locale.getString("migration.status.summary")}
                    </div>
                    <div class="collapsible-body">
                        <p class="left-align">
                            <#list summary as summaryLine>
                                ${summaryLine}<br>
                            </#list>
                        </p>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</#if>

<script>
    migrationStatus = "${status.name()}";
</script>