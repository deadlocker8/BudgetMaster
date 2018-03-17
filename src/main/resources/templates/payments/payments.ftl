<html>
    <head>
        <#import "../header.ftl" as header>
        <@header.header/>
        <link type="text/css" rel="stylesheet" href="/css/categories.css"/>
        <#assign locale = localization["tools.Localization"]>
    </head>
    <body class="budgetmaster-blue-light">
        <#import "../navbar.ftl" as navbar>
        <@navbar.navbar "payments"/>

        <main>
            <div class="card main-card">
                <#import "../datePicker.ftl" as datePicker>
                <@datePicker.datePicker currentDate currentDateFormatted/>
                <div class="hide-on-small-only"><br></div>
                <div class="row">
                    <div class="col s6 center-align"><a href="/categories/newIncome" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">file_download</i>${locale.getString("title.payment.new", locale.getString("title.income"))}</a></div>
                    <div class="col s6 center-align"><a href="/categories/newPayment" class="waves-effect waves-light btn budgetmaster-blue"><i class="material-icons left">file_upload</i>${locale.getString("title.payment.new", locale.getString("title.payment"))}</a></div>
                </div>
                <br>
                <div class="container">
                    <table class="bordered">
                        <#list payments as payment>
                        <tr>
                            <td>
                                <div class="category-circle" style="background-color: ${payment.category.color}">
                                    <span style="color: ${payment.category.getAppropriateTextColor()}">
                                        ${payment.category.name?capitalize[0]}
                                    </span>
                                </div>
                            </td>
                            <td>${payment.name}</td>
                            <td>
                                <a href="/payments/${payment.ID}/edit" class="btn-flat no-padding"><i class="material-icons left">edit</i></a>
                                <#if (payment.category.type.name() != "REST")>
                                    <a href="/payments/${payment.ID}/requestDelete" class="btn-flat no-padding"><i class="material-icons left">delete</i></a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                    </table>
                </div>
            </div>

            <#--<#if currentCategory??>-->
                <#--<!-- confirm delete modal &ndash;&gt;-->
                <#--<div id="modalConfirmDelete" class="modal">-->
                    <#--<div class="modal-content">-->
                        <#--<h4>Kategorie löschen</h4>-->
                        <#--<p>Möchtest du die Kategorie "${currentCategory.name}" wirklich löschen?</p>-->
                    <#--</div>-->
                    <#--<div class="modal-footer">-->
                        <#--<a href="/categories" class="modal-action modal-close waves-effect waves-red btn-flat ">Abbrechen</a>-->
                        <#--<a href="/categories/${currentCategory.ID}/delete" class="modal-action modal-close waves-effect waves-green btn-flat ">Löschen</a>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</#if>-->
        </main>

        <!--  Scripts-->
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
        <script src="/js/main.js"></script>
        <script src="/js/datePicker.js"></script>
    </body>
</html>