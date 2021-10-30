import React, {useEffect, useState} from 'react';
import {PaginationListCard} from "./PaginationListCard/PaginationListCard";
import style from './PaginationList.module.scss';
import {PaginationSelector} from "../PaginationSelector/PaginationSelector";
import {useLocation} from "react-router-dom";
import Loader from "react-loader-spinner";
import classNames from "classnames";
import {useDispatch, useSelector} from "react-redux";
import {setFilter} from "../../store/actions/filterAction";
import {SizeCounter} from "../SizeCounter/SizeCounter";

export const PaginationList = ({
                                   items,
                                   loading,
                                   deleteAction,
                                   onChangeSize,
                                   count,
                                   onChangePage
}) => {
    const filter = useSelector(store => store.filter);

    const dispatch = useDispatch();

    const handleChangePage = (page) => {
        dispatch(setFilter({
            ...filter,
            pageIdx: page
        }))
        onChangePage && onChangePage(page);
    }

    const getMaxPage = () => {
        return Math.max(Math.ceil(count / filter.pageSize), 1)
    }

    return (
        <div className={style.PaginationList}>
           <div className={classNames(style.PaginationList__list, {
               [style.PaginationList__list_loading]: loading
           })}>
               {loading ?
                       <Loader
                           type="Grid" color="#40C9FF" height={150} width={150}
                       />
                   :
               items && Array.isArray(items) && items.map((item) => {
                   return (
                       <PaginationListCard
                           index={item.id}
                           label={item.name}
                           data={item}
                           deleteAction={deleteAction}
                       />
                   )
               })}
           </div>
            <SizeCounter onChangeSize={(size) => onChangeSize(size)}/>
            <PaginationSelector
                minPage={1}
                maxPage={getMaxPage()}
                activePage={filter.pageIdx}
                onChangePage={(page) => handleChangePage(page)}
            />
        </div>
    )
}