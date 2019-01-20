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

            <#assign currentYear = currentDate.getYear()/>
            <div id="global-datepicker-select-year">
                <@datepickerGridYear currentYear-7 currentYear/>
            </div>

            <div id="global-datepicker-select-month">
                <h4>${locale.getString("title.datepicker.month")}</h4>
                <#assign montList = helpers.getMonthList()/>
                <#assign currentMonth = montList[currentDate.getMonthOfYear() - 1]/>
                <@datepickerGrid montList currentMonth/>
            </div>
        </div>
        <div class="modal-footer background-color">
            <a href="#" class="modal-action modal-close waves-effect waves-light red btn-flat white-text">${locale.getString("cancel")}</a>
            <a href="<@s.url '/setDate?target=${target}'/>" id="buttonChooseDate" class="modal-action modal-close waves-effect waves-light green btn-flat white-text">${locale.getString("ok")}</a>
        </div>
    </div>
</#macro>

<#macro datepickerGridYear startYear currentYear>
    <h4>${locale.getString("title.datepicker.year")}</h4>

    <div class="hidden" id="currentYear">${currentYear?c}</div>

    <div class="center-align">
        <a class="waves-effect text-color global-datepicker-button" id="global-datepicker-previous"><i class="material-icons icon-chevron">chevron_left</i></a>
        <a class="waves-effect text-color global-datepicker-button" id="global-datepicker-next"><i class="material-icons icon-chevron">chevron_right</i></a>
    </div>

    <#assign years = [] />
    <#list startYear..startYear + 11 as i>
        <#assign years = years + [i?c] />
    </#list>
    <@datepickerGrid years currentYear?c/>
</#macro>

<#macro datepickerGrid items currentItem>
<table class="no-border-table global-datepicker-table">
    <#list items as item>
        <#if item?index == 0 || item?index == 3 || item?index == 6 || item?index == 9>
            <tr>
        </#if>
        <td class="global-datepicker-item <#if item == currentItem>global-datepicker-selected</#if>">${item}</td>
        <#if item?index == 2 || item?index == 5 || item?index == 8 || item?index == 11>
            </tr>
        </#if>
    </#list>
</table>

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
        buttonClose = '${locale.getString("ok")}';
    </script>
</#macro>