import React, {useRef, useState} from "react";
import {useRequest} from "../../../hooks/useRequest";
import style from "../Modal.module.scss";
import Input from "../../../common/Input/Input";
import Button from "../../../common/Button/Button";
import classnames from "classnames";

export const MORE_HEIGHT = "MORE_HEIGHT";
export const EYE_COLOR = "EYE_COLOR";
export const EYE_COLOR_NATIONALITY = "EYE_COLOR_NATIONALITY";
export const LESS_LOCATION = "LESS_LOCATION";

export const FilterModal = ({type}) => {
   const [object, setObject] = useState({});
    const [searched, setSearched] = useState(false);

    const [location, setLocation] = useState({
        x: 0,
        y: 0,
        z: 0,
    })

    const [height, setHeight] = useState("");
    const [eyeColor, setEyeColor] = useState("");
    const [nationality, setNationality] = useState("");
    const [errorMessage, setErrorMessage] = useState();

    const inputRef = useRef(null);
    const buttonRef = useRef(null);

    const { getItem, getFilteredItems, getEyeColor } = useRequest();

    const getInputs = (type) => {
        switch (type) {
            case LESS_LOCATION: 
            return <>
                    <Input
                        value={location.x}
                        type="number"
                        setValue={(val) => setLocation({...location, x: val})}
                    />
                    <Input
                        value={location.y}
                        type="number"
                        setValue={(val) => setLocation({...location, y: val})}
                    />
                    <Input
                        value={location.z}
                        type="number"
                        setValue={(val) => setLocation({...location, z: val})}
                    />
                </>
            case MORE_HEIGHT:
                return <Input
                        value={height}
                        type="number"
                        setValue={setHeight}
                    />
            case EYE_COLOR:
                return <Input
                        value={eyeColor}
                        type="text"
                        setValue={setEyeColor}
                    />
            case EYE_COLOR_NATIONALITY:
                return <>
                    <Input
                        label="Цвет глаз"
                        value={eyeColor}
                        type="text"
                        setValue={setEyeColor}
                    />
                    <Input
                        label="Национальность"
                        value={nationality}
                        type="text"
                        setValue={setNationality}
                    />
                </>
            default: 
                return null;
        }
    }

    const getGreaterHeight = async () => {
        let result = await getItem(`moreHeight/${height}`);
        if (!result.error){
            setObject(result.message)
        } else {
            setError(result);
        }
    };

    const getLessLocation = async () => {
        let result = await getFilteredItems({
            locationX: location.x,
            locationY: location.y,
            locationZ: location.z
        }, true, '/lessLocation');
        if (!result.error) {
            setObject(result.results)
        } else {
            setError(result);
        }
    }

    const setError = (result) => {
        if (result.error && result.message && !result.message.includes('xml')){
            setErrorMessage(result.message);
        } else {
            setErrorMessage(result);
        }
    }

    const getEye = async () => {
        let result = await getEyeColor(eyeColor, type === EYE_COLOR_NATIONALITY && nationality);
        if (!result.error){
            setObject(result.message)
        } else {
            setError(result);
        }
    }

    const onClick = () => {
        setObject(null);
        setErrorMessage(null);
        if (type === LESS_LOCATION) {
            getLessLocation();
        } else if (type === MORE_HEIGHT) {
            getGreaterHeight();
        } else {
            getEye();
        }
    }

    const getTitle = (type) => {
        switch (type) {
            case EYE_COLOR_NATIONALITY:
                return "Цвет глаз и национальность";
            case EYE_COLOR:
                return "Цвет глаз";
            case MORE_HEIGHT:
                return "Высота больше";
            default:
            case LESS_LOCATION:
                return "Локация меньше";
        }
    }
    return (
        <div className={style.View}>
            <h4 className={style.View__title}>
                {getTitle(type)}
            </h4>
            <div className={classnames(style.View__control, {
                [style.View__control_easy]: true
            })}>
                {getInputs(type)}
                <Button
                    label="Найти"
                    _ref={buttonRef}
                    onClick={onClick}
                />
            </div>
            {typeof object === "object" && object !== null
                ? Object.keys(object).length > 0 && (
                <pre className={style.View__content}>
                    {Object.keys(object).length > 0 &&
                    JSON.stringify(object, undefined, 4)
                    }
                </pre>)
                : <p className={style.View__content}>{object}</p>
            }
            {errorMessage && <p className={classnames(style.View__content,{
                [style.View__content_alert]: true
            })}>{errorMessage}</p>}
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