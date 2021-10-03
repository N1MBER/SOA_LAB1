import React, {useEffect, useState, useRef} from 'react';
import style from './Selector.module.scss';
import classnames from 'classnames';

const Selector = ({
                            label,
                            required,
                            accordionTitle,
                            list,
                            disabled,
                            withoutPositionsMargin,
                            toggleState,
                            smallMode,
                            closeOnSelect,
                            activeElement,
                            onHoverList,
                            onBlurList,
                            withAdditionalList,
                            additionalList,
                            clickOnElement,
                            defaultZIndex
                        }) => {
    const [listOpened, setListOpened] = useState(false);
    const [height, setHeight] = useState(0);
    const [isVisibleAdditionalList, setIsVisibleAdditionalList] = useState(false);

    const listRef = useRef(null);

    useEffect(() => {
        document.addEventListener('ScanMenuHide', () => {
            setListOpened(false)
        })
        return () => {
            document.removeEventListener('ScanMenuHide', () => {})
        }
    }, [])

    useEffect(() => {
        if (!disabled) {
            if (listOpened) {
                let height = 27 + list.length * 35;
                if (!smallMode)
                    height += (list.length - 1) * 10;
                setHeight(height);
            } else {
                setHeight(0);
            }
        }
    }, [listOpened]);

    const onHoverListItem = (position) => {
        setIsVisibleAdditionalList(true);
        let listPosition = listRef.current.getBoundingClientRect();
        if (onHoverList)
            onHoverList({
                ...position,
                bottom: (listPosition.bottom - position.bottom) + 35,
                right: - listPosition.width,
                element: position.element
            });
    }

    const clickOnListItem = (item) => {
        clickOnElement(item);
        if (closeOnSelect)
            setListOpened(false);
    }

    const onBlurListItem = (position) => {
        setIsVisibleAdditionalList(false);
        let listPosition = listRef.current.getBoundingClientRect();
        if (onBlurList)
            onBlurList({
                ...position,
                top: (listPosition.bottom - position.bottom) + 35,
                right: - listPosition.width,
            });
    }

    useEffect(() => {
        if (disabled) {
            setListOpened(false);
            setHeight(0);
        }
    }, [disabled])

    return (
        <div
            className={style.container}
            style={{zIndex: defaultZIndex? defaultZIndex: 4}}
            onMouseLeave={() => onBlurListItem(listRef.current.getBoundingClientRect())}
        >
            <p className={style.label}>
                {label}
                {required &&
                    <span className={style.label__star}>*</span>
                }
            </p>
            <button
                className={classnames(style.accordionButton, {
                    [style.accordionButton_disabled]: disabled ||
                        (!toggleState && typeof toggleState === 'boolean'),
                    [style.accordionButton_open]: listOpened,
                    [style.accordionButton_top_margin]: !label
                })}
                disabled={!!disabled ||
                    (!toggleState && typeof toggleState === 'boolean')
                }
                onClick={() => setListOpened(!listOpened)}
            >
                <div className={style.accordionButton__content}>
                    <p>{accordionTitle}</p>
                    {AccordionArrow}
                </div>
            </button>
            <div
                style={{
                    height: height,
                    overflow: height <= 300 ?
                        'hidden': 'scroll'
                }}
                ref={listRef}
                className={classnames(style.list, {
                    [style.list_small]: smallMode ? !!smallMode: withoutPositionsMargin,
                    [style.list_hide]: !listOpened,
                    [style.list_open]: listOpened
                })}
            >
                {list && Array.isArray(list) &&
                    list.map((item, index) => {
                        return (
                           <ScanMenuSelectItem
                                _key={`accordion${label}${index}`}
                                clickOnElement={(item) => clickOnListItem(item)}
                                isActiveElement={activeElement === item}
                                item={item}
                                withAdditionalList={withAdditionalList}
                                onBlurElement={(position) => onBlurListItem(position)}
                                onHoverElement={(position) => onHoverListItem(position)}
                           />
                        )
                    })
                }
            </div>
            {(listOpened
            && withAdditionalList
            && isVisibleAdditionalList) &&
                additionalList
            }
        </div>
    )
}

const ScanMenuSelectItem = ({
                                _key,
                                clickOnElement,
                                isActiveElement,
                                onHoverElement,
                                withAdditionalList,
                                item,
                            }) => {

    const itemButtonRef = useRef(null);

    const hoverOnPosition = () => {
        let object = itemButtonRef.current.getBoundingClientRect();
        object.element = item;
        onHoverElement(object);
    }


    return (
        <button
            ref={itemButtonRef}
            className={classnames(style.list__button, {
                [style.list__button_active]: isActiveElement,
            })}
            key={_key}
            onClick={() => clickOnElement(item)}
            onMouseEnter={() => hoverOnPosition()}
        >
            <div className={style.listButton}>
                <p className={style.listButton__text}>{typeof item !== 'object' ? item: item.text}</p>
            </div>
            {item.icon && item.icon}
            {withAdditionalList && AccordionElementArrow}
            {!withAdditionalList && isActiveElement && ActiveArrow}
        </button>
    )
}

const AccordionArrow = (
    <svg width="16" height="10" viewBox="0 0 16 10" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path fillRule="evenodd" clipRule="evenodd" d="M0.305908 1.71984L1.69418 0.280151L8.2331 6.58554L14.2797 0.306354L15.7204 1.69364L8.28551 9.41445L0.305908 1.71984Z" fill="#292929"/>
    </svg>
);

const AccordionElementArrow = (
    <svg width="8" height="12" viewBox="0 0 8 12" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M1 11L6 5.81482L1 1" stroke="#292929" strokeWidth="2"/>
    </svg>
);

const ActiveArrow = (
    <svg width="12" height="11" viewBox="0 0 12 11" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M1 4.47826L5.7619 9L11 1" stroke="url(#paint0_linear)" strokeWidth="2"/>
        <defs>
            <linearGradient id="paint0_linear" x1="5.61111" y1="4.91111" x2="0.999849" y2="4.91979" gradientUnits="userSpaceOnUse">
                <stop stopColor="#40C9FF"/>
                <stop offset="1" stopColor="#4088FF"/>
            </linearGradient>
        </defs>
    </svg>
);

export default Selector;
