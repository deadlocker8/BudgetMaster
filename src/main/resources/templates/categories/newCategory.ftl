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
        <@header.style "iconSelect"/>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "categories" settings/>

        <#import "categoriesFunctions.ftl" as categoriesFunctions>
        <#import "../helpers/iconSelect.ftl" as iconSelectMacros>
        <#import "../helpers/fontColorPicker.ftl" as fontColorPickerMacros>

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
                                    <input id="category-name" type="text" name="name" <#if !isNameEditingForbidden><@validation.validation "name"/></#if> value="${categoryName}" <#if isNameEditingForbidden>readonly</#if>>
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
                                <#-- add custom color picker -->
                                <div class="col s2 m1 no-padding">
                                    <div id="customColorPickerContainer" class="category-color <#if customColor == category.getColor()>category-color-active</#if>" style="background-color: ${customColor}">
                                        <span>+</span>
                                    </div>
                                </div>
                            </div>

                            <#-- icon -->
                            <@iconSelectMacros.iconSelect id="category-icon" item=category/>

                            <#-- font color -->
                            <@fontColorPickerMacros.fontColorPicker category/>

                            <br>

                            <#-- buttons -->
                            <div class="row hide-on-small-only">
                                <div class="col s6 right-align">
                                    <@header.buttonLink url='/categories' icon='clear' localizationKey='cancel' color='red'/>
                                </div>

                                <div class="col s6 left-align">
                                    <@header.buttonSubmit name='action' icon='save' localizationKey='save' color='green'/>
                                </div>
                            </div>
                            <div class="hide-on-med-and-up">
                                <div class="row center-align">
                                    <div class="col s12">
                                        <@header.buttonLink url='/categories' icon='clear' localizationKey='cancel' color='red'/>
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

        <@iconSelectMacros.modalIconSelect idToFocusOnClose="category-name" item=category/>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/webjars/vanilla-picker/2.12.1/dist/vanilla-picker.min.js'/>"></script>
        <script src="<@s.url '/js/categories.js'/>"></script>
        <script src="<@s.url '/js/iconSelect.js'/>"></script>
        <script src="<@s.url '/js/fontColorPicker.js'/>"></script>
    </@header.body>
</html>