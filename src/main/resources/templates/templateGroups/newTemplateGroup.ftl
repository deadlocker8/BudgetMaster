<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <#if templateGroup.getID()??>
            <#assign title=locale.getString("title.template.group.edit")/>
        <#else>
            <#assign title=locale.getString("title.template.group.new")/>
        </#if>

        <@header.header "BudgetMaster - ${title}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "templates" settings/>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${title}</div>
                    </div>
                </div>

                <@header.content>
                    <div class="container">
                    <#import "../helpers/validation.ftl" as validation>
                    <form name="NewTemplateGroup" action="<@s.url '/templateGroups/newTemplateGroup'/>" method="post" onsubmit="return validateForm(true)">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="ID" value="<#if templateGroup.getID()??>${templateGroup.getID()?c}</#if>">

                        <#-- template group name -->
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <i class="material-icons prefix">file_copy</i>
                                <input id="template-group-name" type="text" name="name" <@validation.validation "name"/> value="<#if templateGroup.getName()??>${templateGroup.getName()}</#if>">
                                <label for="template-group-name">${locale.getString("transaction.new.label.name")}</label>
                            </div>
                        </div>

                        <br>

                        <#-- buttons -->
                        <div class="row hide-on-small-only">
                            <div class="col s6 right-align">
                                <@header.buttonLink url='/templateGroups' icon='clear' localizationKey='cancel' color='red'/>
                            </div>

                            <div class="col s6 left-align">
                                <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='green'/>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <@header.buttonLink url='/templateGroups' icon='clear' localizationKey='cancel' color='red'/>
                                </div>
                            </div>
                            <div class="row center-align">
                                <div class="col s12">
                                    <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='green'/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                </@header.content>
            </div>
        </main>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/helpers.js'/>"></script>
        <script src="<@s.url '/js/templateGroups.js'/>"></script>
    </@header.body>
</html>
