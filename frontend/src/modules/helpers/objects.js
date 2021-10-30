export const PersonStructure = {
    id: {
        type: 'number'
    },
    name: {
        type: 'text'
    },
    coordinates_x: {
        type: 'number'
    },
    coordinates_y: {
        type: 'number'
    },
    height: {
        type: 'number'
    },
    passportID: {
        type: 'text'
    },
    hairColor: {
        type: 'select',
        list: ['GREEN', 'WHITE', 'BROWN']
    },
    nationality: {
        type: 'select',
        list: ['VATICAN', 'THAILAND', 'JAPAN']
    },
    location_x: {
        type: 'number'
    },
    location_y: {
        type: 'number'
    },
    location_z: {
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