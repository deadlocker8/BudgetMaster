$(document).ready(function()
{
    if($('.category-select-wrapper').length)
    {
        let categorySelectTrigger = document.querySelector('.category-select-wrapper');
        categorySelectTrigger.addEventListener('click', function()
        {
            openCategorySelect('.category-select-wrapper');
        });

        categorySelectTrigger.addEventListener("keydown", function(event)
        {
            if(event.key === "Escape")
            {
                closeCustomSelect('.category-select-wrapper');
            }

            jumpToItemByFirstLetter('.category-select-wrapper', event.key)
        });

        for(const option of document.querySelectorAll(".category-select-option"))
        {
            option.addEventListener('click', function(event)
            {
                confirmCategory(this);
                event.stopPropagation();
            })
        }

        window.addEventListener('click', function(e)
        {
            let categorySelect = document.querySelector('.custom-select')

            if(!categorySelect.contains(e.target))
            {
                closeCustomSelect('.category-select-wrapper');
                resetSelectedCategoryId();
                removeSelectionStyleClassFromAll();
            }
        });

        enableCategorySelectHotKeys();
    }
});

function openCategorySelect(selector)
{
    let categorySelectTrigger = document.querySelector(selector);
    categorySelectTrigger.querySelector('.custom-select').classList.toggle('open');
    let categoryItems = document.getElementsByClassName('category-select-option');
    selectCustomSelectItem(categoryItems, getIndexOfCustomSelectItemId(categoryItems, resetSelectedCategoryId()));
}

function closeCustomSelect(selector)
{
    document.querySelector(selector + ' .custom-select').classList.remove('open');
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
            openCategorySelect('.category-select-wrapper');
        }
    });
}

let selectedCategoryId = null;
if($('.category-select-wrapper').length)
{
    resetSelectedCategoryId();
}

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

    let items = document.getElementsByClassName('category-select-option');
    let previousIndex = getIndexOfCustomSelectItemId(items, selectedCategoryId);

    // select next item
    if(isUp)
    {
        selectNextCustomSelectItemOnUp(items, previousIndex);
    }
    else
    {
        selectNextCustomSelectItemOnDown(items, previousIndex);
    }
}

function removeSelectionStyleClassFromAll()
{
    let items = document.getElementsByClassName('category-select-option');
    for(let i = 0; i < items.length; i++)
    {
        toggleCustomSelectItemSelection(items[i], false);
    }
}

function getIndexOfCustomSelectItemId(items, id)
{
    for(let i = 0; i < items.length; i++)
    {
        let currentCategoryId = getCustomSelectItemId(items[i]);
        if(currentCategoryId === id)
        {
            return i;
        }
    }

    return null;
}

function getCustomSelectItemId(item)
{
    return item.dataset.value;
}

function selectCustomSelectItem(items, index)
{
    toggleCustomSelectItemSelection(items[index], true);
    items[index].scrollIntoView({
        behavior: 'smooth',
        block: 'start'
    });
    selectedCategoryId = getCustomSelectItemId(items[index]);
}

function toggleCustomSelectItemSelection(item, isSelected)
{
    item.classList.toggle('custom-select-option-hovered', isSelected);
}

function confirmCategorySelection()
{
    let categoryItems = document.querySelectorAll('.category-select-option');
    let index = getIndexOfCustomSelectItemId(categoryItems, selectedCategoryId);
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
    closeCustomSelect('.category-select-wrapper');
}

function selectNextCustomSelectItemOnDown(items, previousIndex)
{
    let isLastItemSelected = previousIndex + 1 === items.length;
    if(isLastItemSelected)
    {
        selectCustomSelectItem(items, 0);
    }
    else
    {
        selectCustomSelectItem(items, previousIndex + 1);
    }
}

function selectNextCustomSelectItemOnUp(items, previousIndex)
{
    let isFirstItemSelected = previousIndex === 0;
    if(isFirstItemSelected)
    {
        selectCustomSelectItem(items, items.length - 1);
    }
    else
    {
        selectCustomSelectItem(items, previousIndex - 1);
    }
}

function jumpToItemByFirstLetter(selector, firstLetter)
{
    let items = document.querySelectorAll(selector + ' .category-select-option');
    let index = getIndexOfItemStartingWithLetter(items, firstLetter);
    if(index !== null)
    {
        removeSelectionStyleClassFromAll();
        selectCustomSelectItem(items, index);
    }
}

function getIndexOfItemStartingWithLetter(items, letter)
{
    for(let i = 0; i < items.length; i++)
    {
        let name = items[i].querySelector('.custom-select-item-name').textContent;
        if(name.toLowerCase().startsWith(letter.toLowerCase()))
        {
            return i;
        }
    }

    return null;
}
