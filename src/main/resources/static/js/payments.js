$( document ).ready(function() {
    $('#modalConfirmDelete').modal('open');

    $('select').material_select();

    $('.datepicker').pickadate({
        selectMonths: true,
        selectYears: 100,
        firstDay: 1,

        // Strings and translations
        monthsFull: monthNames,
        monthsShort: monthNamesShort,
        weekdaysFull: weekDays,
        weekdaysShort: weekDaysShort,
        weekdaysLetter: weekDaysLetters,

        // Buttons
        today: buttonToday,
        clear: buttonClear,
        close: buttonClose,

        // Accessibility labels
        labelMonthNext: '',
        labelMonthPrev: '',
        labelMonthSelect: '',
        labelYearSelect: '',

        // Formats
        format: 'dd.mm.yyyy',
        formatSubmit: 'dd.mm.yyyy'
    });
});