<#import "/spring.ftl" as s>
<#import "../helpers/validation.ftl" as validation>


<#macro buttonNew>
    <@header.buttonLink url='/templateGroups/newTemplateGroup' icon='add' localizationKey='title.template.group.new'/>
</#macro>

<#macro buttonEditTemplateGroups>
    <@header.buttonLink url='/templates' icon='file_copy' localizationKey='menu.templates'/>
</#macro>

<#macro buttons>
    <div class="row valign-wrapper hide-on-small-only">
        <div class="col s6 right-align">
            <@buttonNew/>
        </div>

        <div class="col s6 left-align">
            <@buttonEditTemplateGroups/>
        </div>
    </div>
    <div class="hide-on-med-and-up">
        <div class="row valign-wrapper center-align">
            <div class="col s12">
                <@buttonNew/>
            </div>
        </div>
        <div class="row valign-wrapper center-align">
            <div class="col s12">
                <@buttonEditTemplateGroups/>
            </div>
        </div>
    </div>
</#macro>
