<#global locale = static["de.thecodelabs.utils.util.Localization"]>
<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>

<div class="row">
    <div class="col s12 m12 l8 offset-l2">
        <div class="headline-small migration-status"><i class="${status.getIcon()} ${status.getTextColor()} migration-status-icon"></i> ${locale.getString(status.getLocalizationKey())}</div>
    </div>
</div>

<div class="row left-align">
    <div class="col s12 m12 l8 offset-l2">
        <#list summary as summaryLine>
            ${summaryLine}<br>
        </#list>
    </div>
</div>

<script>
    migrationStatus = "${status.name()}";
</script>