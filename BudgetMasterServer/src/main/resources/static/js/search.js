$(document).ready(function()
{
    $('.page-link').click(function()
    {
        if(!this.parentElement.classList.contains('disabled'))
        {
            let page = this.dataset.page;
            let inputPageNumber = document.getElementById('inputPageNumber');
            inputPageNumber.value = page;
            document.getElementById('searchForm').submit();
        }
    });
});