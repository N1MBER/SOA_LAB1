import React, {useState, useEffect} from 'react';
import classnames from 'classnames';
import style from './Input.module.scss';

export const HELP_ADDRESS = 'HELP_ADDRESS';

const Input = ({
                           label,
                           value,
                           type,
                           _ref,
                           setValue,
                           disabled,
                           required,
                           placeholder,
                           incorrect,
                           message }) => {

    const changeValue = (event) => {
        setValue(event.target.value);
    }

    return (
        <div className={style.container}>
            <p className={style.label}>
                {label}
                {required &&
                    <span className={style.label__star}>*</span>
                }
            </p>
            <div className={classnames(style.inputContainer, {
                [style.inputContainer_disabled]: disabled
            })}>
                <input
                    className={classnames(style.inputContainer__input,{
                        [style.inputContainer__input_red]: incorrect
                    })}
                    value={value}
                    type={type}
                    ref={_ref}
                    placeholder={placeholder}
                    onChange={changeValue}
                    disabled={disabled}
                />
            </div>
            {message &&
                <p className={classnames(style.container__message, {
                    [style.container__message_red]: incorrect
                })}>
                    {message}
                </p>
            }
        </div>
    )
}

export default Input;
