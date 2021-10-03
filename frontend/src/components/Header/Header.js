import React from "react";
import style from './Header.module.scss';
import {useHistory} from "react-router";
import {useLocation} from "react-router-dom";
import classNames from "classnames";
import {coordinate_page, location_page, person_page, start_page} from "../../modules/api";

export const Header = () => {

    const history = useHistory();
    const location = useLocation();

    const routes = [
        {
            label: 'Старт',
            link: start_page
        },
        {
            label: 'Person',
            link: `${person_page}/1`
        },
        {
            label: 'Location',
            link: `${location_page}/1`
        },
        {
            label: 'Coordinate',
            link: `${coordinate_page}/1`
        },
    ]

    const changePage = (route) => {
        if (!location.pathname.includes(route.label.split('/')[1]))
            history.push(route.link)
    }

    return (
        <header className={style.Header}>
            <h1  className={style.Header__title}>
                Lab1
                <span  className={style.Header__title__label}>
                    Колесников М.В.
                </span>
            </h1>
            <div className={style.Header__menu}>
                {routes.map(route =>
                    <button
                        onClick={() => changePage(route)}
                        className={classNames(style.Header__menu__item, {
                            [style.Header__menu__item_active]:
                                location.pathname.includes(route.link.split('/')[1])
                        })}
                    >
                        {route.label}
                    </button>
                )}
            </div>
        </header>
    )
};