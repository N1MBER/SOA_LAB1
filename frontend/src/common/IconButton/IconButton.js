import React from "react";
import style from "./IconButton.module.scss";

export const IconButton = ({
    label,
    icon,
    action
                           }) => {

    return (
        <button
            className={style.button}
            onClick={() => action()}
        >
            <div className={style.button__container}>
                <img
                    className={style.button__container__image}
                    src={icon}
                    alt={label}
                />
            </div>
            <div className={style.button__label}>
                {label}
            </div>
        </button>
    )

}