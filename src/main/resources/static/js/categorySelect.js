$(document).ready(function()
{
    let categorySelectTrigger = document.querySelector('.category-select-wrapper');
    categorySelectTrigger.addEventListener('click', function()
    {
        openCategorySelect();
    });

    categorySelectTrigger.addEventListener("keydown", function(event)
    {
        if(event.key === "Escape")
        {
            closeCategorySelect();
        }
    });

    for(const option of document.querySelectorAll(".category-select-option"))
    {
        option.addEventListener('click', function()
        {
            confirmCategory(this);
        })
    }

    window.addEventListener('click', function(e)
    {
        let categorySelect = document.querySelector('.category-select')

        if(!categorySelect.contains(e.target))
        {
            closeCategorySelect();
            resetSelectedCategoryId();
            removeSelectionStyleClassFromAll();
        }
    });

    enableCategorySelectHotKeys();
});

function openCategorySelect()
{
    let categorySelectTrigger = document.querySelector('.category-select-wrapper');
    categorySelectTrigger.querySelector('.category-select').classList.toggle('open');
    let categoryItems = document.getElementsByClassName('category-select-option');
    selectCategoryItem(categoryItems, getIndexOfCategoryId(categoryItems, resetSelectedCategoryId()));
}

function closeCategorySelect()
{
    let categorySelectTrigger = document.querySelector('.category-select-wrapper');
    categorySelectTrigger.querySelector('.category-select').classList.remove('open');
}

function enableCategorySelectHotKeys()
{
    Mousetrap.bind('up', function()
    {
        handleCategorySelectKeyUpOrDown(true);
    });

    Mousetrap.bind('down', function()
    {
        handleCategorySelectKeyUpOrDown(false);
    });

    Mousetrap.bind('enter', function(event)
    {
        if(isSearchFocused())
        {
            return;
        }

        if(isCategorySelectFocused())
        {
            confirmCategorySelection();
        }
        else
        {
            openCategorySelect();
        }
    });
}

let selectedCategoryId = null;
resetSelectedCategoryId();

function resetSelectedCategoryId()
{
    let categorySelector = document.querySelector('#category-select-selected-category');
    let categoryId = categorySelector.querySelector('.category-circle').dataset.value;
    selectedCategoryId = categoryId
    return categoryId;
}

function handleCategorySelectKeyUpOrDown(isUp)
{
    removeSelectionStyleClassFromAll();

    let categoryItems = document.getElementsByClassName('category-select-option');
    let previousIndex = getIndexOfCategoryId(categoryItems, selectedCategoryId);

    // select next item
    if(isUp)
    {
        selectNextCategoryItemOnUp(categoryItems, previousIndex);
    }
    else
    {
        selectNextCategoryItemOnDown(categoryItems, previousIndex);
    }
}

function removeSelectionStyleClassFromAll()
{
    let categoryItems = document.getElementsByClassName('category-select-option');
    for(let i = 0; i < categoryItems.length; i++)
    {
        toggleCategoryItemSelection(categoryItems[i], false);
    }
}

function getIndexOfCategoryId(categoryItems, categoryId)
{
    for(let i = 0; i < categoryItems.length; i++)
    {
        let currentCategoryId = getCategoryId(categoryItems[i]);
        if(currentCategoryId === categoryId)
        {
            return i;
        }
    }

    return null;
}

function getCategoryId(categoryItem)
{
    return categoryItem.dataset.value;
}

function selectCategoryItem(categoryItems, index)
{
    toggleCategoryItemSelection(categoryItems[index], true);
    categoryItems[index].scrollIntoView({
        behavior: 'smooth',
        block: 'start'
    });
    selectedCategoryId = getCategoryId(categoryItems[index]);
}

function toggleCategoryItemSelection(categoryItem, isSelected)
{
    categoryItem.classList.toggle('category-select-option-hovered', isSelected);
}

function confirmCategorySelection()
{
    let categoryItems = document.querySelectorAll('.category-select-option');
    let index = getIndexOfCategoryId(categoryItems, selectedCategoryId);
    confirmCategory(categoryItems[index]);
}

function confirmCategory(categoryItem)
{
    categoryItem.parentNode.querySelector('.category-select-option.selected').classList.remove('selected');
    categoryItem.classList.add('selected');

    let categorySelector = document.querySelector('#category-select-selected-category');
    categorySelector.innerHTML = categoryItem.innerHTML;

    let categoryCircle = categorySelector.querySelector('.category-circle');
    categoryCircle.classList.add('no-margin-left');
    categoryCircle.dataset.value = categoryItem.dataset.value;

    document.getElementById('hidden-input-category').value = categoryItem.dataset.value;

    resetSelectedCategoryId();
    removeSelectionStyleClassFromAll();
    closeCategorySelect();
}

function selectNextCategoryItemOnDown(categoryItems, previousIndex)
{
    let isLastItemSelected = previousIndex + 1 === categoryItems.length;
    if(isLastItemSelected)
    {
        selectCategoryItem(categoryItems, 0);
    }
    else
    {
        selectCategoryItem(categoryItems, previousIndex + 1);
    }
}

function selectNextCategoryItemOnUp(categoryItems, previousIndex)
{
    let isFirstItemSelected = previousIndex === 0;
    if(isFirstItemSelected)
    {
        selectCategoryItem(categoryItems, categoryItems.length - 1);
    }
    else
    {
        selectCategoryItem(categoryItems, previousIndex - 1);
    }
}
