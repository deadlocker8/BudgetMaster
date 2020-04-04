<#import "/spring.ftl" as s>

<#macro buttonNew>
    <a href="<@s.url '/templates/newTemplate'/>" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">add</i>${locale.getString("title.template.new")}</a>
</#macro>

<#macro buttons>
    <div class="row valign-wrapper">
        <div class="col s12 center-align">
            <@buttonNew/>
        </div>
    </div>
</#macro>