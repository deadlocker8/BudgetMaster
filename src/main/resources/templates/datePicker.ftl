<#macro datePicker fullDate target>
     <div class="container">
         <div class="section center-align">
             <a href="/previousMonth?target=${target}" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-chevron">chevron_left</i></a>
             <a id="datePicker" class="waves-effect grey-text text-darken-4 headline-date">${helpers.getDateStringWithMonthAndYear(fullDate)}</a>
             <a href="/nextMonth?target=${target}" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-chevron">chevron_right</i></a>
             <a href="/today?target=${target}" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-today">event</i></a>
         </div>
     </div>
</#macro>