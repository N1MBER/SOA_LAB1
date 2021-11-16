import React from "react";
import style from "./IconButton.module.scss";

export const IconButton = ({
    label,
    icon,
    action,
    text,
    ...props
                           }) => {

    return (
        <button
            className={style.button}
            onClick={() => action()}
            {...props}
        >
            {icon
                ? <div className={style.button__container}>
                        <img
                            className={style.button__container__image}
                            src={icon}
                            alt={label}
                        />
                </div>
                :<p className={style.button__text}>{text}</p>
            }
            <div className={style.button__label}>
                {label}
            </div>
        </button>
    )

}