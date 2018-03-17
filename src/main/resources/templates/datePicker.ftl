<#macro datePicker fullDate formatteDate>
     <div class="container">
         <div class="section center-align">
             <a href="/previousMonth?currentDate=${fullDate}" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-chevron">chevron_left</i></a>
             <a id="datePicker" class="waves-effect grey-text text-darken-4 headline-date">${formatteDate}</a>
             <a href="/nextMonth?currentDate=${fullDate}" class="waves-effect grey-text text-darken-4"><i class="material-icons icon-chevron">chevron_right</i></a>
         </div>
     </div>
</#macro>