import React, {useEffect, useState} from "react";
import Input from "../../common/Input/Input";
import Button from "../../common/Button/Button";
import style from './Filters.module.scss';
import Selector from "../../common/Selector/Selector";
import {useSelector} from "react-redux";
import {IconButton} from "../../common/IconButton/IconButton";
import createIcon from '../../assets/images/icons/expand.svg';
import filterIcon from '../../assets/images/icons/filter-1634626-1389150.png';
import {useLocation} from "react-router";

const FILTER_MODE = "FILTER_MODE";
const UPDATE_MODE = "UPDATE_MODE";
const CREATE_MODE = "CREATE_MODE";

export const Filters = ({
                     object,
                     method,
                     objectStructure,
                     onSubmitAction,
                     onActiveFilter
}) => {
    const [mode, setMode] = useState(FILTER_MODE);

    const [values, setValues] = useState({});
    const data = useSelector((store) => store.object);

    const location = useLocation();

    useEffect(() => {
        let obj = {};
        Object.keys(objectStructure).forEach(key => {
            if (objectStructure[key].defaultValue)
                obj[key] = objectStructure[key].defaultValue;
        })
        setValues(obj);
    }, [objectStructure])

    useEffect(() => {
        if (Object.keys(data.changeObject).length > 0) {
            setValues(data.changeObject)
            setMode(UPDATE_MODE);
        }
    }, [data.changeObject])

    useEffect(() => {
        setMode(FILTER_MODE);
    }, [location.pathname])

    const setValue = (key, value) => {
        setValues({
            ...values,
            [key]: value.target
                ? value.target.value
                : value
        })
    }

    const getTitle = (mode) => {
        switch (mode) {
            default:
            case FILTER_MODE:
                return `Filter ${object}`;
            case UPDATE_MODE:
                return `Update ${object}`;
            case CREATE_MODE:
                return `Create ${object}`;
        }
    }

    const changeMode = (type) => {
        clearValues();
        setMode(type);
    }

    const clearValues = () => {
        let val = {};
        Object.keys(values).forEach(key => {
            val[key] = '';
        })
        setValues(val);
    }

    return (
        <div className={style.Filters}>
            <div className={style.Filters__container}>
                <h3 className={style.Filters__title}>{getTitle(mode)}</h3>
                <div className={style.Filters__container__panel}>
                    <IconButton
                        label={'Создать'}
                        icon={createIcon}
                        action={() => changeMode(CREATE_MODE)}
                    />
                    <IconButton
                        label={'Фильтры'}
                        icon={filterIcon}
                        action={() => changeMode(FILTER_MODE)}
                    />
                </div>
            </div>
            {objectStructure &&
                <div className={style.Filters__form}>
                    {Object.keys(objectStructure).map(key => {
                        if (key !== 'id')
                            switch (objectStructure[key].type){
                                case 'text':
                                case 'number':
                                    return (
                                        <Input
                                            label={key}
                                            type={objectStructure[key].type}
                                            value={values[key]}
                                            setValue={(value) => setValue(key, value)}
                                            placeholder={key}
                                        />
                                    );
                                case 'select':
                                    return (
                                        <Selector
                                            label={key}
                                            accordionTitle={values[key] || key}
                                            list={objectStructure[key].list}
                                            clickOnElement={(value) => setValue(key, value)}
                                            defaultZIndex={4}
                                            closeOnSelect
                                        />
                                    )
                            }
                    })}
                </div>
            }
            <Button
                label={'Применить'}
                onClick={onSubmitAction}
            />
        </div>
    )
}