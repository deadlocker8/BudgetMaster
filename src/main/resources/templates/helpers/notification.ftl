<#macro notification>
    <#if model["notifications"]??>
        <#if model["notifications"]?size == 1>
            <div id="notificationModal" class="modal">
                <div class="modal-content">
                    <h5>${model["notifications"][0].messageTitle}</h5>
                    <#if model["notifications"][0].messageHeader??><h6>${model["notifications"][0].messageHeader}</h6></#if>
                    <p>${model["notifications"][0].messageBody}</p>
                </div>
                <div class="modal-footer">
                    <a class="modal-action modal-close waves-effect waves-light green btn-flat white-text">OK</a>
                </div>
            </div>
        <#else>
             <div id="notificationModal" class="modal">
                 <div class="modal-content">
                     <h5>Mehrere Meldungen</h5>
                     <#list model["notifications"] as notification>
                        <p>
                            ${notification.messageTitle}:
                            <#if notification.messageHeader??>${notification.messageHeader}</#if>
                            ${notification.messageBody}
                        </p>
                     </#list>
                 </div>
                 <div class="modal-footer">
                     <a class="modal-action modal-close waves-effect waves-light green btn-flat white-text">OK</a>
                 </div>
             </div>
        </#if>
    </#if>
</#macro>