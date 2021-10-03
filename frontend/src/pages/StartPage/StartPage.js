import React from "react";
import personIcon from '../../assets/images/icons/person-1767893-1502146.png';
import locationIcon from '../../assets/images/icons/svg+location+locator+map+navigation+user+user+location+icon-1320184910707394703.png';
import coordinateIcon from '../../assets/images/icons/coordinate-6.png';

import {coordinate_page, location_page, person_page, start_page} from "../../modules/api";
import {useHistory} from "react-router";

import style from './StartPage.module.scss';

export const StartPage = () => {
    const history = useHistory();

    const routes = [
        {
            icon: personIcon,
            label: 'Person',
            link: `${person_page}/1`
        },
        {
            icon: locationIcon,
            label: 'Location',
            link: `${location_page}/1`
        },
        {
            icon: coordinateIcon,
            label: 'Coordinate',
            link: `${coordinate_page}/1`
        },
    ]

    return (
        <div className={style.StartPage}>
            <div className={style.StartPage__info}>
                <h1>Лабораторная работа №1</h1>
                <h3>Колесников М.В.</h3>
                <h4>Вариант: 1010</h4>
            </div>
            <div className={style.StartPage__links}>
                {routes.map(route =>
                    <div className={style.StartPage__links__link}>
                        <button
                            className={style.StartPage__links__button}
                            onClick={() => history.push(route.link)}
                        >
                            <img
                                className={style.StartPage__links__button__image}
                                src={route.icon}
                                alt={''}
                            />
                        </button>
                        <div className={style.StartPage__links__button__label}>
                            {route.label}
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
};