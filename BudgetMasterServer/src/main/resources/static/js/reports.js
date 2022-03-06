$(document).ready(function()
{
    let el = document.getElementById('columnNames');
    Sortable.create(el, {
        animation: 150,
        ghostClass: 'columnName-selected',
        dragClass: 'columnName-selected'
    });

    $('.columnName-checkbox').click(function()
    {
        updateRow(this);
    });

    $('.columnName-checkbox').each(function(i, obj)
    {
        updateRow(obj);
    });
});

function updateRow(item)
{
    if(item.checked)
    {
        $(item).parent().parent().removeClass('columnName-disabled');
    } else
    {
        $(item).parent().parent().addClass('columnName-disabled');
    }
}

function validateForm()
{
    $('.columnName-checkbox').each(function(i, obj)
    {
        let positionInput = document.getElementsByName("columns['" + obj.dataset.index + "'].position")[0];
        positionInput.value = i;
    });

    return true;
}