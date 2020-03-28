<#import "/spring.ftl" as s>

<#macro buttonNew>
    <a href="<@s.url '/templates/newTemplate'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.template.new")}</a>
</#macro>

<#macro buttonShow>
    <a href="<@s.url '/templates'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">file_copy</i>${locale.getString("home.menu.templates.action.show")}</a>
</#macro>

<#macro buttons>
    <div class="row hide-on-small-only valign-wrapper">
        <div class="col s6 right-align">
            <@buttonNew/>
        </div>
        <div class="col s6 left-align">
            <@buttonShow/>
        </div>
    </div>

    <div class="hide-on-med-and-up center-align">
        <div class="row center-align">
            <div class="row center-align">
                <div class="col s12">
                    <@buttonShow/>
                </div>
            </div>
            <div class="col s12">
                <@buttonNew/>
            </div>
        </div>
    </div>
</#macro>