<#macro datePicker fullDate target>
    <#import "/spring.ftl" as s>
     <div class="container">
         <div class="section center-align">
             <a href="<@s.url '/previousMonth?target=${target}'/>" class="waves-effect text-color"><i class="material-icons icon-chevron">chevron_left</i></a>
             <a href="#modalDate" class="waves-effect headline-date modal-trigger text-color datePicker-fixed-width">${helpers.getDateStringWithMonthAndYear(fullDate)}</a>
             <a href="<@s.url '/nextMonth?target=${target}'/>" class="waves-effect text-color"><i class="material-icons icon-chevron">chevron_right</i></a>
             <a href="<@s.url '/today?target=${target}'/>" class="waves-effect text-color"><i class="material-icons icon-today">event</i></a>
         </div>
     </div>
    <!-- modal to select specific month and year -->
    <div id="modalDate" class="modal modal-fixed-footer">
        <div class="modal-content background-color">
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
        <div class="modal-footer background-color">
            <a href="${target}" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
            <a href="<@s.url '/setDate?target=${target}'/>" id="buttonChooseDate" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("ok")}</a>
        </div>
    </div>
</#macro>

<#macro datePickerLocalization>
    <#-- localization for scripts -->
    <script>
        <#assign monthNames = "">
        <#assign monthNamesShort = "">
        <#list helpers.getMonthList() as monthName>
            <#assign monthNames += "'" + monthName + "', ">
            <#assign monthNamesShort += "'" + monthName[0..2] + "', ">
        </#list>

        <#assign weekDays = "">
        <#assign weekDaysShort = "">
        <#assign weekDaysLetters = "">
        <#list helpers.getWeekDays() as weekDay>
            <#assign weekDays += "'" + weekDay + "', ">
            <#assign weekDaysShort += "'" + weekDay[0..1] + "', ">
            <#assign weekDaysLetters += "'" + weekDay[0] + "', ">
        </#list>

        monthNames = [${monthNames}];
        monthNamesShort = [${monthNamesShort}];
        weekDays = [${weekDays}];
        weekDaysShort = [${weekDaysShort}];
        weekDaysLetters = [${weekDaysLetters}];
        buttonToday = '${locale.getString("today")}';
        buttonClear = '';
        buttonClose = '${locale.getString("ok")}';
    </script>
</#macro>