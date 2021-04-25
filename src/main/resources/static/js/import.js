$(document).ready(function()
{
    // prevent form submit on enter
    $(document).on("keypress", 'form', function(e)
    {
        let code = e.keyCode || e.which;
        if(code === 13)
        {
            e.preventDefault();
            return false;
        }
    });

    $('.import-entity-help-button').click(function()
    {
        let modalTitle = document.getElementById('modal-import-entity-help-title');
        modalTitle.innerHTML = this.dataset.title;

        let modalContent = document.getElementById('modal-import-entity-help-content');
        modalContent.innerHTML = this.dataset.text;

        let modalElement = document.getElementById('modal-import-entity-help');
        let modalInstance = M.Modal.getInstance(modalElement);
        modalInstance.open();
    });
});

function validateForm()
{
    // handle account matches
    let accountSourcesIDs = $('.account-source-id');
    let accountSourcesNames = $('.account-source');

    let accountDestinations = $('select.account-destination');
    let parent = document.getElementById("hidden-account-matches");

    for(let i = 0; i < accountSourcesIDs.length; i++)
    {
        let inputSourceID = document.createElement("input");
        inputSourceID.setAttribute("type", "hidden");
        inputSourceID.setAttribute("name", "accountMatches[" + i + "].accountSource.ID");
        inputSourceID.setAttribute("value", accountSourcesIDs[i].innerText);
        parent.appendChild(inputSourceID);

        let inputSourceName = document.createElement("input");
        inputSourceName.setAttribute("type", "hidden");
        inputSourceName.setAttribute("name", "accountMatches[" + i + "].accountSource.name");
        inputSourceName.setAttribute("value", accountSourcesNames[i].innerText);
        parent.appendChild(inputSourceName);

        let value = accountDestinations[i].value;

        let inputDestinationID = document.createElement("input");
        inputDestinationID.setAttribute("type", "hidden");
        inputDestinationID.setAttribute("name", "accountMatches[" + i + "].accountDestination.ID");
        inputDestinationID.setAttribute("value", value);
        parent.appendChild(inputDestinationID);

        let inputDestinationName = document.createElement("input");
        inputDestinationName.setAttribute("type", "hidden");
        inputDestinationName.setAttribute("name", "accountMatches[" + i + "].accountDestination.name");
        inputDestinationName.setAttribute("value", accountDestinations[i].querySelector('option[value="' + value + '"').innerText);
        parent.appendChild(inputDestinationName);
    }

    return true;
}