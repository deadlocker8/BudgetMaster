<#macro datePicker fullDate target>
    <#assign locale = static["tools.Localization"]>
     <div class="container">
         <div class="section center-align">
             <a href="/previousMonth?target=${target}" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-chevron">chevron_left</i></a>
             <a href="#modalDate" class="waves-effect grey-text text-darken-4 headline-date modal-trigger">${helpers.getDateStringWithMonthAndYear(fullDate)}</a>
             <a href="/nextMonth?target=${target}" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-chevron">chevron_right</i></a>
             <a href="/today?target=${target}" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-today">event</i></a>
         </div>
     </div>
    <!-- modal to select specific month and year -->
    <div id="modalDate" class="modal">
        <div class="modal-content">
            <h4>${locale.getString("title.datepicker")}</h4>
            <div class="input-field col s12">
                <select id="selectMonth">
                    <#list helpers.getMonthList() as monthName>
                        <option <#if currentDate.getMonthOfYear() == monthName?index + 1>selected</#if> value="${monthName?index + 1}">${monthName}</option>
                    </#list>
                </select>
                <label for="selectMonth">${locale.getString("datepicker.label.month")}</label>
            </div>
            <div class="input-field col s12">
                <select id="selectYear">
                    <#list helpers.getYearList() as year>
                        <option <#if currentDate.getYear() == year>selected</#if> value="${year?c}">${year?c}</option>
                    </#list>
                </select>
                <label for="selectYear">${locale.getString("datepicker.label.year")}</label>
            </div>
        </div>
        <div class="modal-footer">
            <a href="${target}" class="modal-action modal-close waves-effect waves-red btn-flat">${locale.getString("cancel")}</a>
            <a href="/setDate?target=${target}" id="buttonChooseDate" class="modal-action modal-close waves-effect waves-green btn-flat">${locale.getString("ok")}</a>
        </div>
    </div>
</#macro>