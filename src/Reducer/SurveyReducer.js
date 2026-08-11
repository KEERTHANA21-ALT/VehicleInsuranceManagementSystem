const initialState = {
    surveys: [],
    loading: false
};

function SurveyReducer(state = initialState, action) {

    switch (action.type) {

        case "GET_ASSIGNED_SURVEYS":

            return {
                ...state,
                surveys: action.payload,
                loading: false
            };


        case "GET_ASSIGNED_SURVEYS_ERROR":

            return {
                ...state,
                surveys: [],
                loading: false
            };


        case "UPDATE_SURVEY":

            return {
                ...state
            };


        default:

            return state
    }
}

export default SurveyReducer