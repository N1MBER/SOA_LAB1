import React from 'react';

import style from './PaginationListCard.module.scss';

import updateIcon from '../../../assets/images/icons/Edit_icon_(the_Noun_Project_30184).svg.png';
import deleteIcon from '../../../assets/images/icons/delete-button.svg';
import getIcon from '../../../assets/images/icons/223930.png';

import {useDispatch} from "react-redux";
import {setModal, setObject} from "../../../store/actions/objectAction";
import {MODAL_MESSAGE, MODAL_VIEW} from "../../Modal/Modal";
import {IconButton} from "../../../common/IconButton/IconButton";
import {API_PERSONS, sendRequest} from "../../../modules/api";
import converter from "xml2js";
import {decomposePersonToNormalView, decomposePersonToOneLayer} from "../../../modules/helpers/decompose";
import {useRequest} from "../../../hooks/useRequest";

export const PaginationListCard = ({
    index,
    label,
    data,
    deleteAction,
                                   }) => {
    const dispatch = useDispatch();

    const {
        getItem,
    } = useRequest();

    const Buttons = [
        {
            label: 'Обновить',
            icon: updateIcon,
            action: (value) => dispatch(setObject(decomposePersonToOneLayer(value))),
        },
        {
            label: 'Просмотреть',
            icon: getIcon,
            action: (id) => getDataById(id),
            key: 'id'
        },
        {
            label: 'Удалить',
            icon: deleteIcon,
            action: (id) => deleteAction && deleteAction(id),
            key: 'id'
        },
    ]

    const getDataById = (id) => {
        getItem(id).then(res => {
            dispatch(setModal({
                type: !res.error
                    ? MODAL_VIEW
                    : MODAL_MESSAGE,
                visible: true,
                data: !res.error
                    ? res
                    : {
                        type: 'error',
                        message: res.message
                    }
            }))
        })
    }

    return (
        <div className={style.PaginationListCard}>
            <div className={style.PaginationListCard__content}>
                <span className={style.PaginationListCard__content__index}>
                    {index}
                </span>
                <div className={style.PaginationListCard__content__text}>
                    {label}
                </div>
            </div>
            <div className={style.PaginationListCard__panel}>
                {Buttons.map(item =>
                    <IconButton
                        key={`${index}key_button${item.label}`}
                        icon={item.icon}
                        label={item.label}
                        action={() => item.action(item.key ? data[item.key] : data)}
                    />
                )}
            </div>
        </div>
    )
}