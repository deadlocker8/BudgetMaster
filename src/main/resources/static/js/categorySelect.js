$(document).ready(function()
{
    let selectorCategorySelect = '.category-select-wrapper';

    if($(selectorCategorySelect).length)
    {
        let categorySelect = new CustomSelect(selectorCategorySelect);

        categorySelect.resetSelectedItemId()

        let customSelectTrigger = document.querySelector(selectorCategorySelect);
        customSelectTrigger.addEventListener('click', function()
        {
            categorySelect.open();
        });

        customSelectTrigger.addEventListener("keydown", function(event)
        {
            if(event.key === "Escape")
            {
                categorySelect.close();
            }

            categorySelect.jumpToItemByFirstLetter(event.key);
        });

        for(const option of document.querySelectorAll(selectorCategorySelect + ' .custom-select-option'))
        {
            option.addEventListener('click', function(event)
            {
                categorySelect.confirmItem(this);
                event.stopPropagation();
            })
        }

        window.addEventListener('click', function(e)
        {
            let expectedTarget = document.querySelector(selectorCategorySelect + ' .custom-select')

            if(!expectedTarget.contains(e.target))
            {
                categorySelect.close();
                categorySelect.resetSelectedItemId();
                categorySelect.removeSelectionStyleClassFromAll();
            }
        });

        categorySelect.enableHotkeys();
    }
});

class CustomSelect
{
    constructor(selector)
    {
        this.selector = selector;
        this.selectedId = null;
    }

    open()
    {
        let trigger = document.querySelector(this.selector);
        trigger.querySelector('.custom-select').classList.toggle('open');
        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        this.resetSelectedItemId();
        this.selectItem(items, this.getIndexOfItemById(items, this.selectedId));
    }

    close()
    {
        document.querySelector(this.selector + ' .custom-select').classList.remove('open');
    }

    enableHotkeys()
    {
        let self = this;
        Mousetrap.bind('up', function()
        {
            self.handleKeyUpOrDown(true);
        });

        Mousetrap.bind('down', function()
        {
            self.handleKeyUpOrDown(false);
        });

        Mousetrap.bind('enter', function()
        {
            if(isSearchFocused())
            {
                return;
            }

            if(isCustomSelectFocused())
            {
                self.confirmSelection(self.selector, self.selectedId);
            }
            else
            {
                self.open(self.selector);
            }
        });
    }

    resetSelectedItemId()
    {
        let itemSelector = document.querySelector(this.selector + ' #custom-select-selected-item');
        this.selectedId = itemSelector.querySelector('.category-circle').dataset.value;
    }

    handleKeyUpOrDown(isUp)
    {
        this.removeSelectionStyleClassFromAll();

        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        let previousIndex = this.getIndexOfItemById(items, this.selectedId);
        console.log("previousIndex " + previousIndex + " this.selectedId " + this.selectedId);

        // select next item
        if(isUp)
        {
            this.selectNextItemOnUp(items, previousIndex);
        }
        else
        {
            this.selectNextItemOnDown(items, previousIndex);
        }
    }

    removeSelectionStyleClassFromAll()
    {
        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        for(let i = 0; i < items.length; i++)
        {
            this.toggleItemSelection(items[i], false);
        }
    }

    getIndexOfItemById(items, id)
    {
        for(let i = 0; i < items.length; i++)
        {
            let currentItemId = this.getItemId(items[i]);
            if(currentItemId === id)
            {
                return i;
            }
        }

        return null;
    }

    getItemId(item)
    {
        return item.dataset.value;
    }

    selectItem(items, index)
    {
        console.log(items);
        console.log(index);
        this.toggleItemSelection(items[index], true);
        items[index].scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });
        this.selectedId = this.getItemId(items[index]);
    }

    toggleItemSelection(item, isSelected)
    {
        item.classList.toggle('custom-select-option-hovered', isSelected);
    }

    confirmSelection()
    {
        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        let index = this.getIndexOfItemById(items, this.selectedId);
        this.selectedId = this.confirmItem(items[index]);
    }

    confirmItem(item)
    {
        // remove old selection
        item.parentNode.querySelector('.custom-select-option.selected').classList.remove('selected');

        item.classList.add('selected');

        let itemSelector = document.querySelector(this.selector + ' #custom-select-selected-item');
        itemSelector.innerHTML = item.innerHTML;

        let itemCircle = itemSelector.querySelector('.category-circle');
        itemCircle.classList.add('no-margin-left');
        itemCircle.dataset.value = item.dataset.value;

        document.querySelector(this.selector + ' .hidden-input-custom-select').value = item.dataset.value;

        this.removeSelectionStyleClassFromAll();
        this.close(this.selector);
        this.selectedId = this.resetSelectedItemId();
    }

    selectNextItemOnDown(items, previousIndex)
    {
        let isLastItemSelected = previousIndex + 1 === items.length;
        if(isLastItemSelected)
        {
            this.selectedId = this.selectItem(items, 0);
        }
        else
        {
            this.selectedId = this.selectItem(items, previousIndex + 1);
        }
    }

    selectNextItemOnUp(items, previousIndex)
    {
        let isFirstItemSelected = previousIndex === 0;
        if(isFirstItemSelected)
        {
            this.selectItem(items, items.length - 1);
        }
        else
        {
            this.selectItem(items, previousIndex - 1);
        }
    }

    jumpToItemByFirstLetter(firstLetter)
    {
        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        let index = this.getIndexOfCustomSelectItemStartingWithLetter(items, firstLetter);
        if(index !== null)
        {
            this.removeSelectionStyleClassFromAll(this.selector);
            this.selectedId = this.selectItem(items, index);
        }
    }

    getIndexOfCustomSelectItemStartingWithLetter(items, letter)
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

}

let selectedCategoryId = null;


