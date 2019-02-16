<#import "/spring.ftl" as s>

<#macro buttons>
    <div class="row hide-on-small-only valign-wrapper">
        <div class="col s6 right-align">
            <@buttonReset/>
        </div>

        <div class="col s6 left-align">
            <@buttonApply/>
        </div>
    </div>

    <div class="hide-on-med-and-up valign-wrapper">
        <div class="row center-align">
            <div class="col s12">
                <@buttonReset/>
            </div>
        </div>
        <div class="row center-align">
            <div class="col s12">
                <@buttonApply/>
            </div>
        </div>
    </div>
</#macro>

<#macro buttonReset>
    <a href="<@s.url '/filter/reset'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">settings_backup_restore</i>${locale.getString("filter.reset")}</a>
</#macro>

<#macro buttonApply>
    <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave">
        <i class="fas fa-filter left"></i>${locale.getString("filter.apply")}
    </button>
</#macro>
