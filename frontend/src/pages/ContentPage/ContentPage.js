import React, {useEffect, useState} from 'react';

import {useLocation} from "react-router-dom";
import { Filters } from "../../components/Filters/Filters";
import style from './ContentPage.module.scss';
import {PaginationList} from "../../components/PaginationList/PaginationList";
import {person_page} from "../../modules/api";
import {PersonStructure} from "../../modules/helpers/objects";
import {useDispatch, useSelector} from "react-redux";
import {setModal, setObject} from "../../store/actions/objectAction";
import {useRequest} from "../../hooks/useRequest";
import {CREATE_MODE, UPDATE_MODE, FILTER_MODE, DELETE_MODE} from "../../modules/helpers";
import {MODAL_MESSAGE} from "../../components/Modal/Modal";
import {setFilter} from "../../store/actions/filterAction";

export const ContentPage = () => {
    const [objectStructure, setObjectStructure] = useState({});
    const [content, setContent] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [count, setCount] = useState(0);

    const location = useLocation();
    const dispatch = useDispatch();

    const {
        getFilteredItems,
        createItem,
        updateItem,
        deleteItem
    } = useRequest();

    const filter = useSelector(store => store.filter);

    useEffect(() => {
        getData(1)
        setObject(PersonStructure)
    }, [location.pathname]);

    const getData = (page) => {
        setIsLoading(true)
        getFilteredItems({pageIdx: page}).then(res => {
            setIsLoading(false)
            if (!res.error){
                setContent(res.results);
                setCount(res.count);
            }
        })
    }

    const openMessageModal = (status, message) => {
        dispatch(setModal({
            type: MODAL_MESSAGE,
            visible: true,
            data: {
                type: status,
                message
            }
        }))
    }

    const submitAction = (data, fromFilter) => {
        switch (data.type) {
            case FILTER_MODE: {
                setIsLoading(true);
                if (fromFilter) {
                    dispatch(setFilter({
                        ...filter,
                        ...data.data,
                        pageIdx: 1
                    }))
                    data.data = {...data.data, pageIdx: 1}
                }
                getFilteredItems(data.data).then(res => {
                    setIsLoading(false)
                    if (!res.error){
                        setContent(res.results);
                        setCount(res.count);
                    }
                })
                return;
            }
            case UPDATE_MODE: {
                updateItem(data.data).then(res => {
                    if (!res.error) {
                        getData(1);
                        openMessageModal('success', 'Successful update')
                    } else {
                        openMessageModal('error', res.message)
                    }
                })
                return;
            }
            case CREATE_MODE: {
                createItem(data.data).then(res => {
                    if (!res.error) {
                        getData(1);
                        openMessageModal('success', 'Successful create')
                    } else {
                        openMessageModal('error', res.message)
                    }
                })
                return;
            }
            case DELETE_MODE: {
                deleteItem(data.data).then(res => {
                    if (!res.error) {
                        getData(1);
                        openMessageModal('success', 'Successful delete')
                    } else {
                        openMessageModal('error', res.message)
                    }
                })
                return;
            }
            default:
                return;
        }
    }

    const getObjectStructureByPathName = (path) => {
        switch (path) {
            default:
                return {};
            case person_page:
                return PersonStructure;
        }
    }

    const changeSize = (size) => {
        submitAction({
            type: FILTER_MODE,
            data: {
                pageSize: size
            }
        })
    }

    const changePage = (page) => {
        submitAction({
            type: FILTER_MODE,
            data: {
                pageIdx: page
            }
        })
    }

    useEffect(() => {
        dispatch(setObject({}));
    }, [location.pathname])

    return (
        <div className={style.ContentPage}>
            <Filters
                object={location
                    .pathname.split('/')[1]
                    .toUpperCase()}
                method={"Post"}
                objectStructure={PersonStructure}
                onSubmitAction={(res) => submitAction(res, true)}
            />
            <PaginationList
                items={content}
                loading={isLoading}
                count={count}
                onChangeSize={(size) => changeSize(size)}
                onChangePage={(page) => changePage(page)}
                deleteAction={(val) => submitAction({type: DELETE_MODE, data: val})}
            />
        </div>
    )
}