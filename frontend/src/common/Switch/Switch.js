import React from 'react';
import classnames from 'classnames';
import style from './Switch.module.scss';

const Switch = ({label,
                            blueMode,
                            clickOnToggle,
                            firstButtonText,
                            secondButtonText,
                            firstButtonIcon,
                            disabled,
                            activeButton,
                            disabledFirstButton,
                            disablesSecondButton,
                            secondButtonIcon}) => {
    return (
        <div className={style.container}>
            <p className={style.container__label}>{label}</p>
            <div className={classnames(style.toggle, {
                [style.toggle_margin_top]: !label
            })}>
                <button
                    disabled={disabled || disabledFirstButton}
                    className={classnames(style.toggle__button, {
                        [style.toggle__button_disabled]: disabled,
                        [style.toggle__button_active]: activeButton === 1,
                        [style.toggle__button_blue]: activeButton === 1 && blueMode
                    })}
                    onClick={() => clickOnToggle(1)}
                >
                    {firstButtonIcon && firstButtonIcon}
                    {firstButtonText}
                </button>
                <button
                    disabled={disabled || disablesSecondButton}
                    className={classnames(style.toggle__button, {
                        [style.toggle__button_disabled]: disabled,
                        [style.toggle__button_active]: activeButton === 2,
                        [style.toggle__button_blue]: activeButton === 2 && blueMode
                    })}
                    onClick={() => clickOnToggle(2)}
                >
                    {secondButtonIcon && secondButtonIcon}
                    {secondButtonText}
                </button>
            </div>
        </div>
    )
}

export default Switch;
