<html>
    <head>
        <#import "/spring.ftl" as s>

        <#import "../helpers/header.ftl" as header>
        <@header.globals/>

        <#if category.getID()??>
            <#assign title=locale.getString("title.category.edit")/>
        <#else>
            <#assign title=locale.getString("title.category.new")/>
        </#if>

        <@header.header "BudgetMaster - ${title}"/>
        <link type="text/css" rel="stylesheet" href="<@s.url '${"/css/libs/spectrum.css"}'/>"/>
        <@header.style "spectrum"/>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "categories" settings/>

        <#import "categoriesFunctions.ftl" as categoriesFunctions>

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
                        <form name="NewCategory" action="<@s.url '/categories/newCategory'/>" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" name="ID" value="<#if category.getID()??>${category.getID()?c}</#if>">
                            <input type="hidden" name="type" value="<#if category.getType()??>${category.getType()}</#if>">

                            <#-- name -->
                            <div class="row">
                                <div class="input-field col s12 m12 l8 offset-l2">
                                    <#assign categoryName=categoriesFunctions.getCategoryName(category)>
                                    <#assign isNameEditingForbidden=category.getType()?? && (category.getType().name() == "NONE" || category.getType().name() == "REST")>

                                    <i class="material-icons prefix">edit</i>
                                    <input id="category-name" type="text" name="name" <@validation.validation "name"/> value="${categoryName}" <#if isNameEditingForbidden>disabled</#if>>
                                    <label for="category-name">${locale.getString("category.new.label.name")}</label>
                                </div>
                            </div>

                            <#-- color -->
                            <input type="hidden" name="color" id="categoryColor" value="${category.getColor()}">
                            <#list categoryColors as color>
                                <#if color?counter == 1 || color?counter == 7 || color?counter == 13>
                                    <div class="row">
                                        <div class="col s2 m1 offset-m3 no-padding">
                                            <div class="category-color <#if color == category.getColor()>category-color-active</#if>" style="background-color: ${color}"></div>
                                        </div>
                                <#else>
                                    <div class="col s2 m1 no-padding">
                                        <div class="category-color <#if color == category.getColor()>category-color-active</#if>" style="background-color: ${color}"></div>
                                    </div>
                                </#if>

                                <#if color?counter == 6 || color?counter == 12>
                                    </div>
                                </#if>
                            </#list>
                                <#--add custom color picker-->
                                <div class="col s2 m1 no-padding">
                                    <div id="customColorPickerContainer" class="category-color <#if customColor == category.getColor()>category-color-active</#if>" style="background-color: ${customColor}">
                                        <input type="text" id="customColorPicker" value="${customColor}" placeholder="+"/>
                                    </div>
                                </div>
                            </div>

                            <#-- icon -->
                            <div class="row">
                                <div class="input-field col s12 m12 l8 offset-l2">
                                    <i class="fas fa-icons prefix"></i>
                                    <label class="input-label" for="category-icon">${locale.getString("category.new.label.icon")}</label>
                                    <div id="category-icon" class="valign-wrapper">
                                        <a href="#modalIconSelect" id="category-icon-preview" class="modal-trigger">
                                            <i id="category-icon-preview-icon" class="<#if category.getIcon()?has_content>${category.getIcon()}<#else>hidden</#if>"></i>
                                            <div id="category-icon-placeholder" class="<#if category.getIcon()?has_content>hidden</#if>">${locale.getString("category.new.icon.placeholder")}</div>
                                        </a>
                                    <@header.buttonFlat url='' icon='delete' id='button-remove-category-icon' localizationKey='' classes="no-padding text-default" noUrl=true/>
                                    </div>
                                    <input id="hidden-input-category-icon" type="hidden" name="icon" value="<#if category.getIcon()??>${category.getIcon()}</#if>">
                                </div>
                            </div>

                            <@categoriesFunctions.modalIconSelect/>

                            <br>

                            <#-- buttons -->
                            <div class="row hide-on-small-only">
                                <div class="col s6 right-align">
                                    <@header.buttonLink url='/categories' icon='clear' localizationKey='cancel'/>
                                </div>

                                <div class="col s6 left-align">
                                    <@header.buttonSubmit name='action' icon='save' localizationKey='save'/>
                                </div>
                            </div>
                            <div class="hide-on-med-and-up">
                                <div class="row center-align">
                                    <div class="col s12">
                                        <@header.buttonLink url='/categories' icon='clear' localizationKey='cancel'/>
                                    </div>
                                </div>
                                <div class="row center-align">
                                    <div class="col s12">
                                        <@header.buttonSubmit name='action' icon='save' localizationKey='save'/>
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
        <script src="<@s.url '/js/libs/spectrum.js'/>"></script>
        <script src="<@s.url '/js/categories.js'/>"></script>
    </@header.body>
</html>