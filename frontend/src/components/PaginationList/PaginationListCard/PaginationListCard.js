import React from 'react';

import style from './PaginationListCard.module.scss';

import updateIcon from '../../../assets/images/icons/Edit_icon_(the_Noun_Project_30184).svg.png';
import deleteIcon from '../../../assets/images/icons/delete-button.svg';
import getIcon from '../../../assets/images/icons/223930.png';

import {useDispatch} from "react-redux";
import {setModal, setObject} from "../../../store/actions/objectAction";
import {MODAL_VIEW} from "../../Modal/Modal";
import {IconButton} from "../../../common/IconButton/IconButton";

export const PaginationListCard = ({
    index,
    label,
    data,
    deleteAction,
                                   }) => {
    const dispatch = useDispatch();

    const Buttons = [
        {
            label: 'Обновить',
            icon: updateIcon,
            action: (value) => dispatch(setObject(value))
        },
        {
            label: 'Просмотреть',
            icon: getIcon,
            action: (value) => {
                dispatch(setModal({
                    type: MODAL_VIEW,
                    visible: true,
                    content: value
                }))
            }
        },
        {
            label: 'Удалить',
            icon: deleteIcon,
            action: () => deleteAction()
        },
    ]

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
                        icon={item.icon}
                        label={item.label}
                        action={() => item.action(data)}
                    />
                )}
            </div>
        </div>
    )
}