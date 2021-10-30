import React from "react";
import {useLocation} from "react-router";
import {useSelector} from "react-redux";
import style from '../Modal.module.scss';

export const ModalView = ({data}) => {
    const location = useLocation();


    return (
        <div className={style.View}>
            <h4 className={style.View__title}>
                {`Просмотр объекта ${location
                .pathname.split('/')[1]
                .toUpperCase()}`}
            </h4>
            <pre className={style.View__content}>
                {JSON.stringify(data, undefined, 4)}
            </pre>
        </div>
    )
}