import React from "react";
import style from './button.module.scss';

const CustomButton = ({label, onClick, _ref}) => {
    return (
        <button
            className={style.button}
            ref={_ref}
            onClick={() => onClick()}
        >
            {label}
        </button>
    )
}

export default CustomButton;
