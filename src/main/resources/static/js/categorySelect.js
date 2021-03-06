$(document).ready(function()
{
    document.querySelector('.category-select-wrapper').addEventListener('click', function()
    {
        this.querySelector('.category-select').classList.toggle('open');
    })

    for(const option of document.querySelectorAll(".category-select-option"))
    {
        option.addEventListener('click', function()
        {
            if(!this.classList.contains('selected'))
            {
                this.parentNode.querySelector('.category-select-option.selected').classList.remove('selected');
                this.classList.add('selected');

                let categorySelector = this.closest('.category-select').querySelector('.category-select__trigger #category-select-selected-category');
                categorySelector.innerHTML = this.innerHTML;
                categorySelector.querySelector('.category-circle').classList.add('no-margin-left');

                document.getElementById('hidden-input-category').value = this.dataset.value;
            }
        })
    }

    window.addEventListener('click', function(e)
    {
        for(const select of document.querySelectorAll('.category-select'))
        {
            if(!select.contains(e.target))
            {
                select.classList.remove('open');
            }
        }
    });
});
