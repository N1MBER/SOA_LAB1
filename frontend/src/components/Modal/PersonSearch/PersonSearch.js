import React, { useState, useRef } from 'react';
import style from "../Modal.module.scss";
import classnames from "classnames";
import Input from "../../../common/Input/Input";
import Button from "../../../common/Button/Button";
import {useRequest} from "../../../hooks/useRequest";

export const PersonSearch = () => {
    const [id, setId] = useState('');
    const [incorrect, setIncorrect] = useState('');
    const [object, setObject] = useState({});
    const [searched, setSearched] = useState(false)

    const inputRef = useRef(null);
    const buttonRef = useRef(null);

    const { getItem } = useRequest();

    const getDataById = () => {
        setSearched(true);
        if (id) {
            getItem(id).then(res => {
                if (res.error) {
                    setObject({});
                } else {
                    setObject(res);
                }
            })
        }
    }

    return (
        <div className={style.View}>
            <h4 className={style.View__title}>
                Поиск конкретного пользователя
            </h4>
            <div className={style.View__control}>
                <Input
                    label="ID пользователя"
                    value={id}
                    _ref={inputRef}
                    type="number"
                    setValue={setId}
                    incorrect={incorrect}
                    message={incorrect ? "Введите корректный id" : ""}
                    placeholder="ID пользователя"
                />
                <Button
                    label="Найти"
                    _ref={buttonRef}
                    onClick={() => getDataById()}
                />
            </div>
            {Object.keys(object).length > 0 && (
                <pre className={style.View__content}>
                    {Object.keys(object).length > 0 &&
                        JSON.stringify(object, undefined, 4)
                    }
                </pre>
            )}
            {searched && Object.keys(object).length === 0 && (
                <h4 className={classnames(style.View__title, {
                    [style.View__title_alert]: true
                })}>
                    Результат не найден
                </h4>
            )}
        </div>
    )
}