import React, {useRef, useState} from "react";
import {useRequest} from "../../../hooks/useRequest";
import style from "../Modal.module.scss";
import Input from "../../../common/Input/Input";
import Button from "../../../common/Button/Button";
import classnames from "classnames";
import {setModal} from "../../../store/actions/objectAction";
import {MODAL_MESSAGE} from "../Modal";

export const MORE_HEIGHT = "MORE_HEIGHT";
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

    const inputRef = useRef(null);
    const buttonRef = useRef(null);

    const { getItem, getFilteredItems } = useRequest();

    const getGreaterHeight = async () => {
        let result = await getItem(`moreHeight/${height}`);
        if (!result.error){
            setObject(result.message)
        }
    };

    const getLessLocation = async () => {
        let result = await getFilteredItems({
            locationX: location.x,
            locationY: location.y,
            locationZ: location.z
        }, false, '/lessLocation');
        if (!result.error) {
            setObject(result.results)
        }
    }

    return (
        <div className={style.View}>
            <h4 className={style.View__title}>
                {type === LESS_LOCATION
                    ? "Локация меньше"
                    : "Высота больше"
                }
            </h4>
            <div className={classnames(style.View__control, {
                [style.View__control_easy]: true
            })}>
                {type === LESS_LOCATION
                    ? <>
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
                    :
                    <Input
                        value={height}
                        type="number"
                        setValue={setHeight}
                    />
                }
                <Button
                    label="Найти"
                    _ref={buttonRef}
                    onClick={() => type === LESS_LOCATION ? getLessLocation() : getGreaterHeight()}
                />
            </div>
            {typeof object === "object"
                ? Object.keys(object).length > 0 && (
                <pre className={style.View__content}>
                    {Object.keys(object).length > 0 &&
                    JSON.stringify(object, undefined, 4)
                    }
                </pre>)
                : <p className={style.View__content}>{object}</p>
            }
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