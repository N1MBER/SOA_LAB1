export const PersonStructure = {
    id: {
        type: 'number'
    },
    name: {
        type: 'text'
    },
    coordinate_X: {
        type: 'number'
    },
    coordinate_Y: {
        type: 'number'
    },
    height: {
        type: 'number'
    },
    passportID: {
        type: 'text'
    },
    color: {
        type: 'select',
        list: ['GREEN', 'WHITE', 'BROWN']
    },
    country: {
        type: 'select',
        list: ['VATICAN', 'THAILAND', 'JAPAN']
    },
    location_X: {
        type: 'number'
    },
    location_Y: {
        type: 'number'
    },
    location_Z: {
        type: 'number'
    },
}

export const CoordinatesStructure = {
    x: {
        type: 'number'
    },
    y: {
        type: 'number'
    }
}

export const LocationStructure = {
    x: {
        type: 'number'
    },
    y: {
        type: 'number'
    },
    z: {
        type: 'number'
    },
}