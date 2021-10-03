import React from 'react';

import style from './PaginationSelector.module.scss';
import classNames from "classnames";

export const PaginationSelector = ({
    minPage,
    maxPage,
    activePage,
    onChangePage
}) => {

    const getListPage = (page, maxPage = 1) => {
        let page_array = [1];
        if (page === 1 && maxPage){
            if (maxPage < 3){
                if (maxPage !== 1){
                    page_array.push(2);
                }
            }else{
                page_array.push(2);
                page_array.push(maxPage);
            }
        } else if (page === 2 && maxPage){
            if (maxPage === 3){
                if (maxPage !== 2){
                    page_array.push(2);
                    page_array.push(3)
                }
            }else if(maxPage === 2){
                page_array.push(2);
            }else {
                page_array.push(2);
                page_array.push(3);
                page_array.push(maxPage);
            }
        } else if (page === maxPage -1){
            page_array.push(page - 1)
            page_array.push(page)
            page_array.push(page + 1)
        } else if (page === maxPage){
            page_array.push(page - 1)
            page_array.push(page)
        }else if ( maxPage) {
            page_array.push(page - 1)
            page_array.push(page)
            page_array.push(page + 1)
            page_array.push(maxPage)
        }
        return page_array;
    }

    return (
        <div className={style.PaginationSelector}>
            <PageButton
                label={'<'}
                disabled={activePage === minPage}
                onClick={() => onChangePage(activePage - 1)}
            />
            {
                getListPage(activePage, maxPage).map(page =>
                    <PageButton
                        label={page}
                        isActive={page === activePage}
                        onClick={() => onChangePage(page)}
                    />
                )
            }
            <PageButton
                label={'>'}
                disabled={activePage === maxPage}
                onClick={() => onChangePage(activePage + 1)}
            />
        </div>
    )
}

const PageButton = ({
    label,
    disabled,
    isActive,
    onClick
}) => {
    return (
        <button
            className={classNames(style.PaginationSelector__button, {
                [style.PaginationSelector__button_active]: isActive,
                [style.PaginationSelector__button_disabled]: disabled
            })}
            disabled={disabled}
            onClick={() => onClick()}
        >
            {label}
        </button>
    )
}