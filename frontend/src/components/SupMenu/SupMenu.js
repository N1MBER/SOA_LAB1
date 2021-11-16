import React, {useState} from 'react';
import classnames from "classnames";
import style from "./SupMenu.module.scss";
import {IconButton} from "../../common/IconButton/IconButton";
import {useRequest} from "../../hooks/useRequest";
import {setModal} from "../../store/actions/objectAction";
import {MODAL_FILTER, MODAL_MESSAGE, MODAL_VIEW} from "../Modal/Modal";
import {useDispatch} from "react-redux";
import {LESS_LOCATION, MORE_HEIGHT} from "../Modal/FilterModal/FilterModal";

export const SupMenu = () => {
    const [open, setOpen] = useState(false);

    const { getFilteredItems, getItem } = useRequest();

    const dispatch = useDispatch();

    const arr = [
        {
            onClick: () => getMinNationality(),
            label: "Min nationality",
            text: "N"
        },
        {
            onClick: () => getGreaterHeight(),
            label: "Greater height",
            text: "H"
        },
        {
            onClick: () => getLessLocation(),
            label: "Less location",
            text: "L"
        },
    ];

    const getMinNationality = async () => {
        let result = await getItem('minNationality');
        if (!result.error){
            dispatch(setModal({
                type: !result.error
                    ? MODAL_VIEW
                    : MODAL_MESSAGE,
                visible: true,
                data: !result.error
                    ? result
                    : {
                        type: 'error',
                        message: result.message
                    }
            }))
        }
    };

    const getGreaterHeight = async () => {
        dispatch(setModal({
            type: MODAL_FILTER,
            visible: true,
            data: MORE_HEIGHT
        }))
    };

    const getLessLocation = () => {
        dispatch(setModal({
            type: MODAL_FILTER,
            visible: true,
            data: LESS_LOCATION
        }))
    };

    return (
        <div className={classnames(style.SupMenu, {
            [style.SupMenu_open]: open
        })}>
            <div className={style.SupMenu__List}>
                {arr.map(item => (
                    <IconButton {...item} />
                ))}
            </div>
            <button
                className={style.SupMenu__Button}
                onClick={() => setOpen(!open)}
            >
                ?
            </button>
        </div>
    )
}