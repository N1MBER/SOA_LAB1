import React, {useState} from 'react';
import {PaginationListCard} from "./PaginationListCard/PaginationListCard";
import style from './PaginationList.module.scss';
import {PaginationSelector} from "../PaginationSelector/PaginationSelector";

export const PaginationList = ({items}) => {
    const [activePage, setActivePage] = useState(1);

    return (
        <div className={style.PaginationList}>
           <div className={style.PaginationList__list}>
               {items && Array.isArray(items) && items.map((item, index) => {
                   return (
                       <PaginationListCard
                           index={item.id || index + 1}
                           label={item.name ||  'item'}
                           data={item}
                       />
                   )
               })}
           </div>
            <PaginationSelector
                minPage={1}
                maxPage={10}
                activePage={activePage}
                onChangePage={(page) => setActivePage(page)}
            />
        </div>
    )
}