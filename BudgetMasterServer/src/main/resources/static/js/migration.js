$(document).ready(function()
{
    getMigrationStatus();
});

function getMigrationStatus()
{
    if(typeof migrationStatus === 'undefined')
    {
        document.getElementById('progress-spinner').style.display = 'none';
    }
    else
    {
        switch(migrationStatus)
        {
            case 'NOT_RUNNING':
                document.getElementById('button-migration-home').style.display = 'none';
                document.getElementById('progress-spinner').style.display = 'none';
                break;
            case 'SUCCESS':
                document.getElementById('button-migration-home').style.display = '';
                document.getElementById('progress-spinner').style.display = 'none';
                break;
            case 'RUNNING':
                document.getElementById('button-migration-home').style.display = 'none';
                document.getElementById('progress-spinner').style.display = '';
                break;
            default:
                document.getElementById('button-migration-home').style.display = 'none';
                document.getElementById('progress-spinner').style.display = 'none';
                return;
        }
    }

    $.ajax({
        type: 'GET',
        url: $('#migration-status').attr('data-url'),
        data: {},
        success: function(data)
        {
            $('#migration-status').html(data);
        },
        complete: function()
        {
            // schedule the next request when the current one is complete
            setTimeout(getMigrationStatus, 3000);
        }
    });
}