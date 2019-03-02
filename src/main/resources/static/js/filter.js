$(document).ready(function () {
    $('.collapsible').collapsible();

    $(':checkbox').change(function() {
        updateStatus();
    });

    $('#filter-name').on('input', function() {
        updateStatus();
    });

    updateStatus();
});

function updateStatus() {
    updateStatusForSectionByCheckboxes('section-type');
    updateStatusForSectionByCheckboxes('section-repeating');
    updateStatusForSectionByCheckboxes('section-categories');
    updateStatusForSectionByCheckboxes('section-tags');
    updateStatusForSectionName('section-name');
}

function updateStatusForSectionByCheckboxes(identifier) {
    var section = document.getElementById(identifier);
    section.querySelector('.collapsible-header-status').innerText = getStatusByCheckboxes(section);
}

function getStatusByCheckboxes(item) {
    var checkboxes = $(item).find('input[type=checkbox]');
    var checkedCount = 0;
    for(var i = 0; i < checkboxes.length; i++) {
        if(checkboxes[i].checked)
        {
            checkedCount += 1;
        }
    }

    return checkedCount + "/" + checkboxes.length;
}
function updateStatusForSectionName(identifier) {
    var section = document.getElementById(identifier);
    var nameValue = section.querySelector('#filter-name').value;
    var statusText = '1/1';

    if(nameValue.length === 0)
    {
        statusText = '0/1';
    }
    section.querySelector('.collapsible-header-status').innerText = statusText;
}