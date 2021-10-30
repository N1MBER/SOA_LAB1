import React, {useState} from 'react';
import classnames from "classnames";
import {useDispatch, useSelector} from "react-redux";
import {setFilter} from "../../store/actions/filterAction";
import style from './SizeCounter.module.scss';

export const SizeCounter = ({onChangeSize}) => {
    const [open, setOpen] = useState(false);

    const dispatch = useDispatch();

    const filter = useSelector(store => store.filter);

    const handleChangeSize = (size) => {
        setOpen(!open);
        dispatch(setFilter({
            ...filter,
            pageSize: size
        }))
        onChangeSize && onChangeSize(size);
    }

    return (
        <div className={classnames(style.SizeCounter, {
            [style.SizeCounter_open]: open
        })}>
            <div className={style.SizeCounter__List}>
                {[50, 20, 10, 5].map(count => (
                    <button
                        className={classnames(style.SizeCounter__ListButton, {
                            [style.SizeCounter__ListButton_active]: count === filter.pageSize
                        })}
                        onClick={() => handleChangeSize(count)}
                    >
                        {count}
                    </button>
                ))}
            </div>
            <button
                className={style.SizeCounter__Button}
                onClick={() => setOpen(!open)}
            >
                {filter.pageSize}
            </button>
        </div>
    )
}