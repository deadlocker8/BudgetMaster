<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header/>
        <link type="text/css" rel="stylesheet" href="/css/categories.css"/>
        <#assign locale = localization["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../navbar.ftl" as navbar>
        <@navbar.navbar "categories"/>

        <main>
            <div class="card main-card">
                <div class="container">
                    <div class="section center-align">
                        <div class="grey-text text-darken-4 headline"><#if model["category"].getID()??>${locale.getString("title.category.edit")}<#else>${locale.getString("title.category.new")}</#if></div>
                    </div>
                </div>
                <div class="container">
                    <form name="NewCategory" action="/categories/newCategory" method="post">
                        <input type="hidden" name="ID" value="<#if model["category"].getID()??>${model["category"].getID()}</#if>">
                        <div class="row">
                            <div class="input-field col s12 m12 l8 offset-l2">
                                <input id="category-name" type="text" name="name" value="<#if model["category"].getName()??>${model["category"].getName()}</#if>">
                                <label for="category-name">${locale.getString("category.new.label.name")}</label>
                            </div>
                        </div>
                        <input type="hidden" name="color" id="categoryColor" value="${model["category"].getColor()}">
                        <#list categoryColors as color>
                            <#if color?counter == 1 || color?counter == 7 || color?counter == 13>
                                <div class="row">
                                    <div class="col s2 m1 offset-m3 no-padding">
                                        <div class="category-color <#if color == model["category"].getColor()>category-color-active</#if>" style="background-color: ${color}"></div>
                                    </div>
                            <#else>
                                <div class="col s2 m1 no-padding">
                                    <div class="category-color <#if color == model["category"].getColor()>category-color-active</#if>" style="background-color: ${color}"></div>
                                </div>
                            </#if>

                            <#if color?counter == 6 || color?counter == 12>
                                </div>
                            </#if>
                        </#list>-
                            <#--add custom color picker-->
                            <div class="col s2 m1 no-padding">
                                <div class="category-color <#if model["customColor"] == model["category"].getColor()>category-color-active</#if>" style="background-color: ${model["customColor"]}">
                                    +
                                </div>
                            </div>
                        </div>

                        <br>
                        <div class="row hide-on-small-only">
                            <div class="col m6 l4 offset-l2 right-align">
                                <a href="/categories" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                            </div>

                            <div class="col m6 l4 left-align">
                                <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="action">
                                    <i class="material-icons left">save</i>${locale.getString("save")}
                                </button>
                            </div>
                        </div>
                        <div class="hide-on-med-and-up">
                            <div class="row center-align">
                                <div class="col s12">
                                    <a href="/categories" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">clear</i>${locale.getString("cancel")}</a>
                                </div>
                            </div>
                            <div class="row center-align">
                                <div class="col s12">
                                    <button class="btn waves-effect waves-light budgetmaster-blue" type="submit" name="buttonSave">
                                        <i class="material-icons left">save</i>${locale.getString("save")}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <!-- notification modal -->
        <#import "../notification.ftl" as notification>
        <@notification.notification/>

        <!--  Scripts-->
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
        <script src="/js/main.js"></script>
        <script src="/js/categories.js"></script>
    </body>
</html>