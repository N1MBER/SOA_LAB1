import React, {useEffect, useMemo, useState} from "react";
import Input from "../../common/Input/Input";
import Button from "../../common/Button/Button";
import style from './Filters.module.scss';
import Selector from "../../common/Selector/Selector";
import {useDispatch, useSelector} from "react-redux";
import {IconButton} from "../../common/IconButton/IconButton";
import createIcon from '../../assets/images/icons/expand.svg';
import filterIcon from '../../assets/images/icons/filter-1634626-1389150.png';
import clearIcon from  '../../assets/images/icons/Subtract.svg';
import {useLocation} from "react-router";
import {CREATE_MODE, FILTER_MODE, UPDATE_MODE} from "../../modules/helpers";
import {constructPersonToNormalView} from "../../modules/helpers/constructors";
import {setFilter} from "../../store/actions/filterAction";
import classnames from "classnames";


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
    const dispatch = useDispatch();

    const filter = useSelector(store => store.filter);

    const [sortField, setSortField] = useState('id');

    useEffect(() => {
        let obj = {};
        Object.keys(objectStructure).forEach(key => {
            if (objectStructure[key].defaultValue)
                obj[key] = objectStructure[key].defaultValue;
        })
        setValues(obj);
    }, [objectStructure])

    useMemo(() => {
        if (Object.keys(data.changeObject).length > 0) {
            setValues(data.changeObject)
            setMode(UPDATE_MODE);
        } else {
            setMode(FILTER_MODE);
        }
    }, [data.changeObject])

    useEffect(() => {
        setSortField(filter.sortField);
    }, [])

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

    const selectSortId = (id) => {
        setSortField(id);
        dispatch(setFilter({
            ...filter,
            sortField: id
        }))
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
                    <IconButton
                        label={'Очистить'}
                        icon={clearIcon}
                        action={() => clearValues()}
                    />
                </div>
            </div>
            {objectStructure &&
                <div className={classnames(style.Filters__form, {
                    [style.Filters__form_filter_mode]: mode === FILTER_MODE
                })}>
                    {mode === FILTER_MODE &&
                        <Selector
                            label={'Sort Field'}
                            accordionTitle={sortField}
                            list={Object.keys(objectStructure)}
                            clickOnElement={(value) => selectSortId(value)}
                            defaultZIndex={4}
                            closeOnSelect
                        />
                    }
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
                onClick={() => onSubmitAction({
                    type: mode,
                    data: values
                })}
            />
        </div>
    )
}