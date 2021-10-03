import React from 'react';
import {PaginationListCard} from "./PaginationListCard/PaginationListCard";
import style from './PaginationList.module.scss';

export const PaginationList = ({items}) => {
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
        </div>
    )
}