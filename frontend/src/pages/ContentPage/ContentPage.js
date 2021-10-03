import React, {useEffect, useState} from 'react';

import {useLocation} from "react-router-dom";
import {Filters} from "../../components/Filters/Filters";
import style from './ContentPage.module.scss';
import {PaginationList} from "../../components/PaginationList/PaginationList";
import {coordinate_page, location_page, person_page} from "../../modules/api";
import {CoordinatesStructure, LocationStructure, PersonStructure} from "../../modules/helpers/objects";
import {constructCoordinate, constructLocation, constructPerson, coord} from "../../modules/helpers/constructors";
import {useDispatch} from "react-redux";
import {setObject} from "../../store/actions/objectAction";

export const ContentPage = () => {
    const [objectStructure, setObjectStructure] = useState({});
    const [content, setContent] = useState([]);

    const location = useLocation();
    const dispatch = useDispatch();

    useEffect(() => {
        setObjectStructure(
            getObjectStructureByPathName(
                `/${location.pathname.split('/')[1]}`
            )
        );
        let arr = [];
        for (let i = 0; i < 10; i++){
            switch (`/${location.pathname.split('/')[1]}`) {
                default:
                    arr.push({});
                case person_page:
                    arr.push(constructPerson({}));
                    break;
                case location_page:
                    arr.push(constructLocation({}));
                    break;
                case coordinate_page:
                    arr.push(coord);
                    break;
            }
        }
        setContent(arr);
    }, [location.pathname]);

    const getObjectStructureByPathName = (path) => {
        switch (path) {
            default:
                return {};
            case person_page:
                return PersonStructure;
            case location_page:
                return LocationStructure;
            case coordinate_page:
                return CoordinatesStructure;
        }
    }

    useEffect(() => {
        dispatch(setObject({}));
    }, [location.pathname])

    return (
        <div className={style.ContentPage}>
            <Filters
                object={location
                    .pathname.split('/')[1]
                    .toUpperCase()}
                method={"Post"}
                objectStructure={objectStructure}
                onSubmitAction={() => {}}
            />
            <PaginationList items={content}/>
        </div>
    )
}