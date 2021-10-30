import React from 'react';
import style from "../Modal.module.scss";
import classnames from "classnames";

export const MessageModal = ({type, message}) => {
    return (
        <div className={style.View}>
            <h4 className={classnames(style.View__title, {
                [style.View__title_alert]: type === 'error',
                [style.View__title_success]: type === 'success',
            })}>
                {type === 'success'
                    ? 'Success'
                    : 'Error'
                }
            </h4>
            <p className={style.View__text}>{message}</p>
        </div>
    )
}