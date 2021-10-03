import React from 'react';
import {Switch, Route, Redirect, useLocation} from "react-router-dom";
import {ContentPage} from "../../pages/ContentPage/ContentPage";
import {StartPage} from "../../pages/StartPage/StartPage";
import {coordinate_page, location_page, person_page, start_page} from "../../modules/api";

export const Router = ({children, ...props}) => {
    const location = useLocation();

    return (
        <Switch>
            <Route
                exact
                path={[
                    person_page,
                    location_page,
                    coordinate_page
                ]}
            >
                <Redirect to={`/${location.pathname}/1`}/>
            </Route>
            <Route exact path={'/'}>
                <Redirect to={start_page}/>
            </Route>
            <Route exact path={start_page}>
                <StartPage/>
            </Route>
            <Route
                exact
                path={[
                    `${person_page}/:id(\\d+)`,
                    `${location_page}/:id(\\d+)`,
                    `${coordinate_page}/:id(\\d+)`
                ]}
            >
                <ContentPage/>
            </Route>
            <Route>
                <Redirect to={start_page}/>
            </Route>
            {children}
        </Switch>
    )
};