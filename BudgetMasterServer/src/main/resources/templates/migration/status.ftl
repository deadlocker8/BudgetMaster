<html>
    <head>
        <#import "../helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('title.migration')}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "../helpers/navbar.ftl" as navbar>
        <@navbar.navbar "migration" settings/>
        <#import "../helpers/validation.ftl" as validation>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline">${locale.getString("title.migration")}</div>
                    </div>
                </div>

                <div class="container center-align">
                    <div class="row">
                        <div class="col s12 m12 l8 offset-l2">
                            <div class="preloader-wrapper small active" id="progress-spinner">
                                <div class="spinner-layer spinner-blue-only">
                                    <div class="circle-clipper left">
                                        <div class="circle"></div>
                                    </div>
                                    <div class="gap-patch">
                                        <div class="circle"></div>
                                    </div>
                                    <div class="circle-clipper right">
                                        <div class="circle"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="migration-status" data-url="<@s.url '/migration/getStatus'/>"></div>

                    <div class="row" id="button-migration-home">
                        <div class="col s12 m12 l8 offset-l2">
                            <@header.buttonLink url='/' icon='home' localizationKey='menu.home'/>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <script>
            migrationStatus = "${status.name()}";
        </script>

        <!-- Scripts-->
        <#import "../helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
        <script src="<@s.url '/js/migration.js'/>"></script>
    </@header.body>
</html>