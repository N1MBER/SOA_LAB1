export const constructPerson = (value) => {
    return {
        id: value['id'] || 0,
        name: value['name'] || 'default',
        coordinate: value['coordinate']
            ? {
                x: value['coordinate_X'] || 0,
                y: value['coordinate_Y'] || 0,
            }
            : {
                x: 0,
                y: 0,
            },
        height: value['height'] || 0,
        passportID: value['passportID'] || 'default',
        color: value['color'] || 'BROWN',
        country: value['country'] || 'JAPAN',
        location: value['location']
            ? {
                x: value['location_X'] || 0,
                y: value['location_X'] || 0,
                z: value['location_X'] || 0,
            }
            : {
                x: 0,
                y: 0,
                z: 0
            },
    }
}

export const constructLocation = (value) => {
    return {
        x: value['x'] || 0,
        y: value['y'] || 0,
        z: value['z'] || 0,
    }
}

export const constructCoordinate = (value) => {
    return {
        x: value['x'] || 0,
        y: value['y'] || 0,
    }
}

export const coord = {
    x: 20,
    y: 20
}