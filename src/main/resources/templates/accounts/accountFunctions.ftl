<#import "../helpers/header.ftl" as header>

<#macro modalAccountIconSelect>
    <div id="modalAccountIconSelect" class="modal modal-fixed-footer background-color">
        <div class="modal-content center-align">
            <div class="row">
                <div class="col s12">
                    <div class="headline">Upload image</div>
                </div>
            </div>

            <div class="row">
                <form id="form-upload-account-image" method="POST" action="/media" enctype="multipart/form-data">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <div class="file-field input-field col s12">
                        <div class="container">
                            <div class="btn background-blue">
                                <i class="material-icons left">folder</i>
                                ${locale.getString("account.new.icon.upload.choose.file")}
                                <input id="inputUploadFile" type="file" accept="image/png, image/jpeg" name="file">
                            </div>
                            <div class="file-path-wrapper" id="abc">
                                <input id="def" class="file-path validate" type="text">
                            </div>
                            <@header.buttonSubmit name='action' icon='upload' localizationKey='account.new.icon.upload' classes="right"/>
                        </div>
                    </div>
                </form>
            </div>

            <hr>

            <div class="row">
                <div class="col s12">
                     <div class="headline">Available images</div>
                </div>
            </div>

            <div class="row" id="available-images">
            </div>
        </div>
        <div class="modal-footer background-color">
            <@header.buttonLink url='' icon='clear' localizationKey='cancel' color='red' classes='modal-action modal-close text-white' noUrl=true/>
            <@header.buttonLink url='' icon='done' id='button-account-icon-confirm' localizationKey='ok' color='green' classes='modal-action modal-close text-white' noUrl=true/>
        </div>
    </div>
</#macro>
