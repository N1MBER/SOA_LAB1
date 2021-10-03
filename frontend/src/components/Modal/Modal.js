import React from "react";
import {ModalView} from "./ModalView/ModalView";
import {useDispatch} from "react-redux";
import style from './Modal.module.scss';
import {setModal} from "../../store/actions/objectAction";

export const MODAL_VIEW = 'MODAL_VIEW';

export const Modal = ({type}) => {
    const dispatch = useDispatch();

    const getModal = (type) => {
        switch (type) {
            default:
                return;
            case MODAL_VIEW:
                return <ModalView />
        }
    }

    return (
        <div className={style.Modal}>
            <div className={style.Modal__container}>
                <button
                    onClick={() => {
                        dispatch(setModal({
                            visible: false
                        }))
                    }}
                    className={style.Modal__container__close}
                >
                    +
                </button>
                {getModal(type)}
            </div>
        </div>
    )
}