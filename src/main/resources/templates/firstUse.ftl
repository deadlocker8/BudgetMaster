<html>
    <head>
        <#import "helpers/header.ftl" as header>
        <@header.globals/>
        <@header.header "BudgetMaster - ${locale.getString('menu.firstUseGuide')}"/>
        <#import "/spring.ftl" as s>
    </head>
    <@header.body>
        <#import "helpers/navbar.ftl" as navbar>
        <@navbar.navbar "firstUseGuide" settings/>

        <#import "indexFunctions.ftl" as indexFunctions>

        <main>
            <div class="card main-card background-color">
                <div class="container">
                    <div class="section center-align">
                        <div class="headline"><i class="fas fa-graduation-cap"></i> ${locale.getString("home.first.use")}</div>
                    </div>
                </div>
                <br>

                <div class="container">
                    <div class="container center-align">
                        <div class="row left-align">
                            <div class="col s12">
                                <@indexFunctions.stepContent headline="home.first.use.step.1.headline" contentText="home.first.use.step.1.contentText" actionUrl="/accounts" actionName="home.menu.accounts.action.manage"/>
                            </div>
                        </div>
                        <hr>

                        <div class="row left-align">
                            <div class="col s12">
                                <@indexFunctions.stepContent headline="home.first.use.step.2.headline" contentText="home.first.use.step.2.contentText" actionUrl="/categories" actionName="home.menu.categories.action.manage"/>
                            </div>
                        </div>
                        <hr>

                        <div class="row left-align">
                            <div class="col s12">
                                <@indexFunctions.stepContent headline="home.first.use.step.3.headline" contentText="home.first.use.step.3.contentText" actionUrl="/transactions/newTransaction/normal" actionName="home.menu.transactions.action.new">
                                    <ul class="browser-default">
                                        <li>${locale.getString("home.first.use.step.3.sub.1")}</li>
                                        <li>${locale.getString("home.first.use.step.3.sub.2")}</li>
                                        <li>${locale.getString("home.first.use.step.3.sub.3")}</li>
                                        <li>${locale.getString("home.first.use.step.3.sub.4")}</li>
                                        <li>${locale.getString("home.first.use.step.3.sub.5")}</li>
                                        <li>${locale.getString("home.first.use.step.3.sub.6")}</li>
                                    </ul>
                                </@indexFunctions.stepContent>
                            </div>
                        </div>
                        <hr>

                        <div class="row left-align">
                            <div class="col s12">
                                <@indexFunctions.stepContent headline="home.first.use.step.4.headline" contentText="home.menu.transactions" actionUrl="/transactions" actionName="home.menu.transactions.action.manage">
                                    <br>
                                    ${locale.getString("home.first.use.step.4.contentText")}
                                    <ul class="browser-default">
                                        <li>${locale.getString("home.first.use.step.4.sub.1")}</li>
                                        <li>${locale.getString("home.first.use.step.4.sub.2")}</li>
                                        <li>${locale.getString("home.first.use.step.4.sub.3")}</li>
                                    </ul>
                                </@indexFunctions.stepContent>
                            </div>
                        </div>
                        <hr>

                        <div class="row left-align">
                            <div class="col s12">
                                <@indexFunctions.stepContent headline="home.first.use.step.5.headline" contentText="home.first.use.step.5.contentText" actionUrl="" actionName="">
                                    <h5>${locale.getString("menu.templates")}</h5>
                                    <p>
                                        ${locale.getString("home.first.use.step.5.sub.1")}
                                    </p>
                                    <p>
                                        <@indexFunctions.action url="/templates" name="home.menu.templates.action.manage"/>
                                    </p>

                                    <h5>${locale.getString("menu.charts")}</h5>
                                    <p>
                                        ${locale.getString("home.first.use.step.5.sub.2")}
                                    </p>
                                    <p>
                                        <@indexFunctions.action url="/charts/manage" name="home.menu.charts.action.manage"/>
                                    </p>

                                    <h5>${locale.getString("menu.reports")}</h5>
                                    <p>
                                        ${locale.getString("home.first.use.step.5.sub.3")}
                                    </p>
                                    <p>
                                        <@indexFunctions.action url="/reports" name="home.menu.reports.action.new"/>
                                    </p>

                                    <h5>${locale.getString("home.first.use.step.5.sub.4")}</h5>

                                    <p class="center-align">
                                        <@header.buttonLink url='/' icon='home' localizationKey='home.first.use.home'/>
                                    </p>
                                </@indexFunctions.stepContent>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!--  Scripts-->
        <#import "helpers/scripts.ftl" as scripts>
        <@scripts.scripts/>
    </@header.body>
</html>
